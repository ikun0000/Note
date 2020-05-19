# explain性能分析



## 能干嘛

* 表读取顺序
* 数据读取操作的操作类型
* 哪些索引可以使用
* 哪些索引被实际使用
* 表之间的引用
* 每张表有多少行被优化器查询



## 用法

```sql
EXPLAIN <SQL Statement>
```

```sql
mysql> explain select * from question;
+----+-------------+----------+------------+------+---------------+------+---------+------+------+----------+-------+
| id | select_type | table    | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra |
+----+-------------+----------+------------+------+---------------+------+---------+------+------+----------+-------+
|  1 | SIMPLE      | question | NULL       | ALL  | NULL          | NULL | NULL    | NULL |   25 |   100.00 | NULL  |
+----+-------------+----------+------------+------+---------------+------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.01 sec)

```

在语句后使用`\G`竖向显示

```sql
mysql> explain select * from user\G;
*************************** 1. row ***************************
           id: 1
  select_type: SIMPLE
        table: user
   partitions: NULL
         type: ALL
possible_keys: NULL
          key: NULL
      key_len: NULL
          ref: NULL
         rows: 1
     filtered: 100.00
        Extra: NULL
1 row in set, 1 warning (0.00 sec)

ERROR: 
No query specified

```



## 输出信息

| 字段          | 意思                                                         |
| ------------- | ------------------------------------------------------------ |
| id            | select查询的序列号，包含一组数字，表示查询执行select字句或操作表的顺序。它有三种情况：id相同，执行顺序由上至下、id不同，如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行、id相同不同，同时存在 |
| select_type   | 查询的类型，主要用于区别普通查询、联合查询、子查询等复杂查询，值的情况为：`SIMPLE`：简单的select查询，`PRIMARY`：查询中包含复杂部分的最外的语句，`SUBQUERY`：子查询，`DERIVED`：在FROM列表中被标记为衍生表，`UNION`：如果第二个select在union之后则被标记，`UNION RESULT`：从union表获取结果的select |
| table         | 显示这一行的数据是关于哪张表的                               |
| partitions    |                                                              |
| type          | 访问类型排列，显示查询使用哪种类型，它的值以最好到最坏排序为（不全）：`system`（只有一行记录，等于系统表） > `const`（通过索引一次就能找到的） > `eq_ref`（唯一性索引扫描） > `ref`（非唯一索引扫描） > `range`（只检索指定范围的行） > `index`（Full Index Scan，只遍历索引树） > `ALL`（全表扫面找到匹配行） |
| possible_keys | 显示可能应用在这张表上的索引，一个或多个。查询涉及字段上的索引会被列出，但不一定被查询实际使用 |
| key           | 实际使用的索引，如果为NULL，则没有使用索引                   |
| key_len       | 表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度。在不损失精度的情况下，长度越短越好。key_len显示的值为索引字段的最大可能长度，并非实际使用的长度 |
| ref           | 显示索引的哪一列被使用，如果可能的话，是一个常数，哪些列或常量被用于查找索引列上的值 |
| rows          | 根据表统计信息及索引选用情况，大致估算出找到所需的记录所需要读取的行数 |
| filtered      |                                                              |
| Extra         | 非常重要的额外信息：Using filesort（使用外部索引排序，MySQL无法利用索引完成的排序称为“文件排序”）、Using tempo'rary（使用了临时表保存了中间信息）、Using index（表示相应的select操作中使用了覆盖索引，避免访问了表的数据行。如果同时出现Using where，表明索引被用来执行索引键值的查找，如果没出现，表示索引用来读取读取数据而非执行查找动作） |

总结：

* id为SQL执行的顺序，越大执行优先级越高（先被执行），相同id则按显示顺序执行
* key为SQL执行时真正用到的索引
* type为什么索引，从最优到最坏：`system` > `const` > `eq_ref` > `ref` > `range` > `index` > `ALL`
* rows为多少行被优化器查询，越小越好
* Extra为额外信息，最好出现Using index、Using where，不要出现Using filesort和Using tempo'rary



## 怎么加索引

1. 索引设置在经常要查询的字段中
2. 单表的话给where后面的列按顺序加多列索引，尽量使用`=`，如果出现的范围查找`>`,`<`,`>=`,`<=`的列就不要加索引，直接跳过范围查找那列
3. 两表连接查询，给`ON`的字段加索引，通常左连接加在右表，右连接加在左表
4. 如果两个表都是左连接，则在右边的两张表加，例如`FROM a LEFT JOIN b ON a.key=b.key LEFT JOIN c ON b.key=c.key`就在b表的key和c表的key添加索引



## SQL语句优化

1. 尽量减少JOIN语句中的NestedLoop（嵌套循环）的循环总次数：“永远用小结果集驱动大结果集”
2. 优先优化NestedLoop的内层循环
3. 保证JOIN语句中被驱动表上的JOIN条件字段已经被索引
4. 当无法保证被驱动表的JOIN条件字段被索引且内存资源充足的前提下，不要太吝惜JoinBuffer的设置
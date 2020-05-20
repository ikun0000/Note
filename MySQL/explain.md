# explain性能分析及调优



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

* **id**：select查询的序列号，包含一组数字，表示查询执行select字句或操作表的顺序。

  > 它有三种情况：
  >
  > * id相同：执行顺序由上至下
  > * id不同：如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
  > * id有相同有不同：数字越大越先被执行，数字一样的则由上到下执行

* **select_type**：查询的类型，主要用于区别普通查询、联合查询、子查询等复杂查询

  > 值的情况为：
  >
  > * `SIMPLE`：简单的SELECT查询
  > * `PRIMARY`：复杂查询中最外层的那条SELECT语句
  > * `SUBQUERY`：子查询
  > * `DERIVED`：在FROM列表中被标记为衍生表，就是`FROM (SELECT * FROM b) bb `，中的SELECT语句形成的表
  > * `UNION`：UNION之后的第二个或者后面的SELECT，比如`SELECT ... UNION SELECT ...`，中的第二个SELECT
  > * `UNION RESULT`：从UNION表获取结果的SELECT

* **table**：  显示这一行的数据是关于哪张表的

* **partitions**：

* **type**：使用了那种类型，是否使用了索引

  > 它的值以最好到最坏排序为（不全）：
  >
  > ```
  > system > const > eq_ref > ref > range > index > ALL
  > ```
  >
  > * `system`：对查询优化之后转化为一个常量，如主键在WHERE中查询就会被转化为一个常量，system是const的特列，在表之后一行记录时就是system
  > * `const`：通过索引一次就能找到的
  > * `eq_ref`：唯一性索引扫描，就是列上设置了UNIQUE INDEX，对于每个索引的键值，只有一条记录匹配
  > * `ref`：非唯一索引扫描，哪些列或常量被用于查找索引列上的值
  > * `range`：只检索给定范围的行，使用一个索引来选择行
  > * `index`：全索引扫描，和ALL的区别就是index只遍历索引树
  > * `ALL`：全表扫描找到匹配的行

* **possible_keys**：显示可能应用在这张表上的索引，一个或多个。查询涉及字段上的索引会被列出，但不一定被查询实际使用

* **key**：实际使用的索引，如果为NULL，则没有使用索引

* **key_len**：表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度。在不损失精度的情况下，长度越短越好。key_len显示的值为索引字段的最大可能长度，并非实际使用的长度

* **ref**：显示索引的哪一列被使用，如果可能的话，是一个常数，哪些列或常量被用于查找索引列上的值

* **rows**：MySQL认为执行查询时必须检查的行数

* **filtered**：

* **Extra**：非常重要的额外信息

  > `Using filesort`：使用外部索引排序，MySQL无法利用索引完成的排序称为“文件排序
  >
  > `Using tempo'rary`：使用了临时表保存了中间信息
  >
  > `Using index`：表示相应的SELECT操作中使用了覆盖索引，避免访问了表的数据行。如果同时出现`Using where`，表明索引被用来执行索引键值的查找，如果没出现，表示索引用来读取读取数据而非执行查找动作



**总结**：

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



## 如何避免索引失效

1. 全值匹配
2. 最佳左前缀法则，如果索引多列，要遵守最左前缀法则。指的是查询从索引的做左前列开始并且不跳过索引中的列，即索引是怎么建的，按照索引的顺序不能跳过
3. 不要在索引列上做任何操作（计算，函数，类型转换），会导致索引失效
4. 存储引擎不能使用索引中范围条件右边的列，即出现范围查找（`>`,`<`,`>=`,`<=`,`LIKE`）右边的索引会失效，所以索引列尽量不要有范围查询
5. 尽量使用覆盖索引（只访问索引列（索引列和查询列一致）），减少使用`SELECT *`
6. MySQL在使用不等于（`!=`或`<>`）的时候无法使用索引，会导致全表扫描
7. `IS NULL`，`IS NOT NULL`无法使用索引
8. `LIKE`以通配符开头（%abc），MySQL索引失效会变成全表扫描的操作，如果必须使用`LIKE`则创建查询列的覆盖索引，并且不要使用`SELECT *`，而使用索引的列
9. 字符串不加单引号索引失效
10. 少用`OR`，用它来连接时会索引失效



## EXISTS(小表驱动大表)

```sql
SELECT ... FROM table WHERE EXISTS (subquery)
```

EXISTS只返回TRUE和FALSE

将主查询的数据放到子查询中做验证，根据验证结果（true或false）来决定主查询的数据结果是否得以保留

例如

```sql
SELECT * FROM A WHERE EXISTS (SELECT 1 FROM B WHERE B.id=A.id)
-- 先执行
SELECT * FROM A 
-- 再执行
SELECT * FROM B WHERE A.id=B.id

-- 普通的子查询
SELECT * FROM A WHERE id IN (SELECT id FROM B)
-- 先执行
SELECT id FROM B
-- 再执行
SELECT * FROM A WHERE A.id=B.id
```

总结就是如果A表数据集小于B表则使用`EXISTS`，否则使用`IN `



## ORDER BY优化

MySQL支持两种方式排序，FileSort和Index，Index效率高，它指MySQL扫描索引本身完成排序，FileSort方式效率低

**ORDER BY使用Index方式排序的情况**

1. `ORDER BY`语句使用索引最左前列，即按索引创建顺序左边的那些，且中间不能断开一个索引
2. 使用`WHERE`子句与`ORDER BY`子句条件列组合满足索引最左前列，即`WHERE`子句有一个条件使用了最左边的的索引，并且让他等于一个常量，`ORDER BY`子句中的索引为`WHERE`条件索引的后面的索引

> KEY a_b_c (a, b, c)
>
> `ORDER BY`能使用索引最左前缀
>
> * `ORDER BY a`
> * `ORDER BY a, b`
> * `ORDER BY a, b, c`
> * `ORDER BY a DESC, b DESC, c DESC`
>
> 
>
> 如果`WHERE`使用了最左前缀定义为常量，则`ORDER BY`能使用索引
>
> * `WHERE a=const ORDER BY b, c`
> * `WHERE a=const AND b=const ORDER BY c`
> * `WHERE a=const AND b>const ORDER BY b, c`
>
> 
>
> 不能使用索引排序
>
> * `ORDER BY a ASC, b DESC, c DESC`：排序不一致
> * `WHERE g=const ORDER BY b, c`：丢失a索引
> * `WHERE a=const ORDER BY c`：丢失b索引
> * `WHERE a=const ORDER BY a, d`：d不是索引的一部分
> * `WHERE a IN (...) ORDER BY b, c`：对于排序来说，多个相等条件也是范围查询



**调优策略**

* 增大`sort_buffer_size`参数设置
* 增大`max_length_for_sort_data`参数设置



## GROUP BY优化

* `GROUP BY`实质就是先排序后分组，遵循索引建的最佳左前缀
* 当无法使用索引列，增大`max_length_for_sort_data`参数设置和增大`sort_buffer_size`参数的设置
* `WHERE`高于`HAVING`，能写在`WHERE`的条件就不要写在`HAVING`里




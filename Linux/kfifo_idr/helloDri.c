#include <linux/init.h>
#include <linux/module.h>
#include <linux/sched.h>
#include <asm/current.h>
#include <linux/version.h>
#include <linux/kdev_t.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <linux/kfifo.h>
#include <linux/idr.h>

MODULE_LICENSE("Dual BSD/GPL");

struct kfifo my_kfifo;

int a = 1;
int b = 2;
int c = 3;
int d = 4;

struct idr my_map;

char *str1 = "string 1";
char *str2 = "string 2";
int id1;
int id2;

static int __init hello_init(void)
{
	int res = kfifo_alloc(&my_kfifo, 1024, GFP_KERNEL);
	if (res) {
		printk(KERN_ALERT "kfifo alloc error!!!");
	}

	printk(KERN_INFO "insert data to kfifo!!!");
	kfifo_in(&my_kfifo, &a, sizeof(int));
	kfifo_in(&my_kfifo, &b, sizeof(int));
	kfifo_in(&my_kfifo, &c, sizeof(int));
	kfifo_in(&my_kfifo, &d, sizeof(int));
	printk(KERN_INFO "kfifo size %d, avail %d\n", kfifo_size(&my_kfifo), kfifo_avail(&my_kfifo));

	printk(KERN_INFO "init map");
	idr_init(&my_map);
	printk(KERN_INFO "insert key-value");
	id1 = idr_alloc(&my_map, str1, 0, 9, GFP_KERNEL);
	id2 = idr_alloc(&my_map, str2, 0, 9, GFP_KERNEL);
	printk(KERN_INFO "get key-value");
	printk(KERN_INFO "%d id -> %s\n", id1, (char *)idr_find(&my_map, id1));
	printk(KERN_INFO "%d id -> %s\n", id2, (char *)idr_find(&my_map, id2));

	return 0;
}

static void __exit hello_exit(void)
{
	printk(KERN_INFO "get data from kfifo");
	int i;
	int n;
	for (i = 0; i < 4; i++) {
		kfifo_out(&my_kfifo, &n, sizeof(int));
		printk(KERN_INFO "get data: %d", n);
	}

	printk(KERN_INFO "kfifo is empty %d\n", kfifo_is_empty(&my_kfifo));
	idr_remove(&my_map, id1);
	idr_remove(&my_map, id2);
	idr_destroy(&my_map);

	kfifo_free(&my_kfifo);
}

module_init(hello_init);
module_exit(hello_exit);

#include <linux/init.h>
#include <linux/module.h>
#include <linux/sched.h>
#include <asm/current.h>
#include <linux/version.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <linux/slab.h>
#include <linux/mm.h>
#include <linux/highmem.h>

MODULE_LICENSE("Dual BSD/GPL");

struct page *ppage = NULL;
void *high_addr = NULL;

static int __init hello_init(void)
{
	// alloc_pages
	ppage = alloc_page(__GFP_HIGHMEM | GFP_KERNEL);
	if (!ppage) {
		printk(KERN_INFO "no memory");
		return -ENOMEM;
	}
	high_addr = kmap(ppage);
	printk(KERN_INFO "map high page to %p", high_addr);
	return 0;
}

static void __exit hello_exit(void)
{
	kunmap(ppage);
	__free_pages(ppage, 0);
}

module_init(hello_init);
module_exit(hello_exit);

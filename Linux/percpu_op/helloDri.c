#include <linux/init.h>
#include <linux/module.h>
#include <linux/sched.h>
#include <asm/current.h>
#include <linux/version.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <linux/slab.h>
#include <linux/mm.h>

MODULE_LICENSE("Dual BSD/GPL");

unsigned long my_percpu[NR_CPUS];

DEFINE_PER_CPU(int, percpu_var);

static int __init hello_init(void)
{
	int nr = get_cpu();
	my_percpu[nr] = 1;
	put_cpu();

	printk(KERN_INFO "percpu_var: %d\n", get_cpu_var(percpu_var));
	get_cpu_var(percpu_var) = get_cpu_var(percpu_var) + 10;
	put_cpu_var(percpu_var);
	return 0;
}

static void __exit hello_exit(void)
{
	printk(KERN_INFO "percpu_var: %d\n", get_cpu_var(percpu_var));
	put_cpu_var(percpu_var);
}

module_init(hello_init);
module_exit(hello_exit);

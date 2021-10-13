#include <linux/build-salt.h>
#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

BUILD_SALT;

MODULE_INFO(vermagic, VERMAGIC_STRING);
MODULE_INFO(name, KBUILD_MODNAME);

__visible struct module __this_module
__section(.gnu.linkonce.this_module) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

#ifdef CONFIG_RETPOLINE
MODULE_INFO(retpoline, "Y");
#endif

static const struct modversion_info ____versions[]
__used __section(__versions) = {
	{ 0xc79d2779, "module_layout" },
	{ 0xffeedf6a, "delayed_work_timer_fn" },
	{ 0x8c03d20c, "destroy_workqueue" },
	{ 0x83c66b, "kmem_cache_destroy" },
	{ 0x5f5f1453, "kmem_cache_free" },
	{ 0x4302d0eb, "free_pages" },
	{ 0x6091b333, "unregister_chrdev_region" },
	{ 0x37a0cba, "kfree" },
	{ 0x8b66e8a1, "cdev_del" },
	{ 0x3fd78f3b, "register_chrdev_region" },
	{ 0xb2fcb56d, "queue_delayed_work_on" },
	{ 0xc5b6f236, "queue_work_on" },
	{ 0xdf9208c0, "alloc_workqueue" },
	{ 0xfaef0ed, "__tasklet_schedule" },
	{ 0x6a5cb5ee, "__get_free_pages" },
	{ 0x80299be8, "kmem_cache_alloc" },
	{ 0xb776a08b, "kmem_cache_create" },
	{ 0x15ba50a6, "jiffies" },
	{ 0x406681dd, "cdev_add" },
	{ 0xb8c2987b, "cdev_init" },
	{ 0x26c2e0b5, "kmem_cache_alloc_trace" },
	{ 0x8537dfba, "kmalloc_caches" },
	{ 0x3eeb2322, "__wake_up" },
	{ 0xdecd0b29, "__stack_chk_fail" },
	{ 0x92540fbf, "finish_wait" },
	{ 0x1000e51, "schedule" },
	{ 0x8c26d495, "prepare_to_wait_event" },
	{ 0xfe487975, "init_wait_entry" },
	{ 0xa1c76e0a, "_cond_resched" },
	{ 0x4e0ecf27, "current_task" },
	{ 0xc5850110, "printk" },
	{ 0xbdfb6dbb, "__fentry__" },
};

MODULE_INFO(depends, "");


MODULE_INFO(srcversion, "32A9CEFD8583C30B2336E40");

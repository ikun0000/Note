#include <linux/module.h>
#include <linux/moduleparam.h>
#include <linux/cdev.h>
#include <linux/fs.h>
#include <linux/wait.h>
#include <linux/poll.h>
#include <linux/sched.h>
#include <linux/slab.h>
#include <linux/atomic.h>
#include <linux/jiffies.h>
#include <asm/msr.h>
#include <linux/timer.h>
#include <linux/slab.h>
#include <linux/interrupt.h>
#include <linux/workqueue.h>
#include <linux/list.h>

#define BUFFER_MAX	(256)
#define OK			(0)
#define ERROR		(-1)

struct todo_struct {
	struct list_head list_entry;
	int priority;
	unsigned int id;
};

struct cdev *gDev;
struct file_operations *gFile;
dev_t devNum;
unsigned int subDevNum = 1;
int reg_major = 223;
int reg_minor = 0;
char buffer[BUFFER_MAX];
/*
struct semaphore sema;
int open_count = 0;
*/
/*
static atomic_t can_open = ATOMIC_INIT(1);
*/
spinlock_t count_lock;
int open_count = 0;

static DECLARE_WAIT_QUEUE_HEAD(wq);
static int flag = 0;

struct kmem_cache *pond;
void *p1 = NULL;
void *two_pages = NULL;

struct workqueue_struct *my_workqueue;


int hello_open(struct inode *p, struct file *f) 
{
	printk(KERN_INFO " hello_open\r\n");
	return 0;
}

int hello_close(struct inode *inode, struct file *file) 
{
	printk(KERN_INFO " hello_close ok\n");
	return 0;
}

/*
int hello_open(struct inode *p, struct file *f) 
{
	spin_lock(&count_lock);
	if (open_count > 0) {
		printk(KERN_INFO " device is busy, hello_open fail\n");
		spin_unlock(&count_lock);
		return -EBUSY;
	}
	open_count++;
	spin_unlock(&count_lock);
	printk(KERN_INFO " hello_open\r\n");
	return 0;
}

int hello_close(struct inode *inode, struct file *file) 
{
	spin_lock(&count_lock);
	open_count--;
	spin_unlock(&count_lock);
	printk(KERN_INFO " hello_close ok\n");
	return 0;
}
*/

/*
int hello_open(struct inode *p, struct file *f) 
{
	if (!atomic_dec_and_test(&can_open)) {
		printk(KERN_INFO " device is busy, hello_open fail\n");
		atomic_inc(&can_open);
		return -EBUSY;
	}
	printk(KERN_INFO " hello_open\r\n");
	return 0;
}

int hello_close(struct inode *inode, struct file *file) 
{
	atomic_inc(&can_open);
	printk(KERN_INFO " hello_close ok\n");
	return 0;
}
*/

/*
int hello_open(struct inode *p, struct file *f) 
{
	down(&sema);
	if (open_count >= 1) {
		up(&sema);
		printk(KERN_INFO " device is busy, hello_open fail\n");
		return -EBUSY;
	}
	open_count++;
	up(&sema);
	printk(KERN_INFO " hello_open\r\n");
	return 0;
}

int hello_close(struct inode *inode, struct file *file) 
{
	down(&sema);
	if (open_count != 1) {
		printk(KERN_INFO " something wrong, hello_close fail\n");
		return -EFAULT;
	}
	open_count--;
	up(&sema);
	return 0;
}
*/

ssize_t sleepy_read(struct file *filp, char __user *u, size_t s, loff_t *l)
{
	printk(KERN_DEBUG "prcess %i (%s) going to sleep\n", current->pid, current->comm);
	wait_event_interruptible(wq, flag != 0);
	flag = 0;
	printk(KERN_DEBUG "awoken %i (%s) wakeup\n", current->pid, current->comm);
	return 0;

}

ssize_t sleepy_write(struct file *filp, const char __user *u, size_t s, loff_t *l)
{
	printk(KERN_DEBUG "process %i (%s) awakeing the readers...", current->pid, current->comm);
	flag = 1;
	wake_up_interruptible(&wq);
	return s;
}

/*
ssize_t hello_write(struct file *f, const char __user *u, size_t s, loff_t *l)
{
	printk(KERN_INFO " hello_write\r\n");
	size_t writelen = 0;
	writelen = BUFFER_MAX > s ? s : BUFFER_MAX;
	if (copy_from_user(buffer, u, writelen)) {
		return -EFAULT;
	}
	return writelen;
}

ssize_t hello_read(struct file *f, char __user *u, size_t s, loff_t *l)
{
	printk(KERN_INFO " hello_read\r\n");
	size_t readlen;
	readlen = BUFFER_MAX > s ? s : BUFFER_MAX;
	if (copy_to_user(u, buffer, readlen)) {
		return -EFAULT;
	}
	return readlen;
}
*/

void my_tasklet_func(unsigned long data)
{
	printk(KERN_INFO "my tasklet is running...\n");
}
DECLARE_TASKLET(my_tasklet, my_tasklet_func, 0);

void my_work1(struct work_struct *ws)
{
	printk(KERN_INFO "my workqueue work\n");
}
DECLARE_WORK(my_wk1, my_work1);

void my_work2(struct work_struct *ws)
{
	printk(KERN_INFO "my workqueue delay work\n");
}
DECLARE_DELAYED_WORK(my_wk2, my_work2);

int hello_init(void)
{
	devNum = MKDEV(reg_major, reg_minor);
	if (OK == register_chrdev_region(devNum, subDevNum, "helloworld")) {
		printk(KERN_INFO " register_chrdev_region ok \n");
	} else {
		printk(KERN_INFO " register_chrdev_region error \n");
		return ERROR;
	}

	printk(KERN_INFO " hello driver init \n");
	gDev = kzalloc(sizeof(struct cdev), GFP_KERNEL);
	gFile = kzalloc(sizeof(struct file_operations), GFP_KERNEL);
	gFile->open = hello_open;
	gFile->release = hello_close;
	/*
	gFile->read = hello_read;
	gFile->write = hello_write;
	*/
	gFile->read = sleepy_read;
	gFile->write = sleepy_write;
	gFile->owner = THIS_MODULE;
	cdev_init(gDev, gFile);
	cdev_add(gDev, devNum, 1);
	/*
	sema_init(&sema, 1);
	*/
	spin_lock_init(&count_lock);

	/* jiffies test */
	unsigned long j, stamp_before, stamp_after;
	j = jiffies;
	stamp_before = j - HZ;
	stamp_after = j + 5*HZ;

	printk(KERN_INFO "j = %lu, before = %lu, after = %lu\n", j, stamp_before, stamp_after);
	printk(KERN_INFO "%d : %d\n", time_after(stamp_after, j), time_before(stamp_before, j));
	printk(KERN_INFO "%d : %d\n", time_after(j, stamp_after), time_before(j, stamp_before));

	unsigned long long time;
	time = rdtsc();
	printk(KERN_INFO "msr time: %llu\n", time);

	pond = kmem_cache_create("pond", 128, 0, SLAB_HWCACHE_ALIGN, NULL);

		
	p1 = kmem_cache_alloc(pond, GFP_KERNEL);
	two_pages = (void *)__get_free_pages(GFP_KERNEL, 2);

	tasklet_schedule(&my_tasklet);

	my_workqueue = create_workqueue("my work queue");
	queue_work(my_workqueue, &my_wk1);
	queue_delayed_work(my_workqueue, &my_wk2, 10 * HZ);


	struct todo_struct todo1, todo2, todo3, todo4, todo5;
	todo1.id = todo1.priority = 1; 
	todo2.id = todo2.priority = 2; 
	todo3.id = todo3.priority = 3; 
	todo4.id = todo4.priority = 4; 
	todo5.id = todo5.priority = 5; 
	INIT_LIST_HEAD(&todo1.list_entry);

	list_add(&todo2.list_entry, &todo1.list_entry);
	list_add(&todo3.list_entry, &todo1.list_entry);
	list_add(&todo4.list_entry, &todo1.list_entry);
	list_add(&todo5.list_entry, &todo1.list_entry);

	printk(KERN_INFO "address head: 0x%llx\n", (unsigned long long)&todo1.list_entry);
	printk(KERN_INFO "address todo1: 0x%llx\n", (unsigned long long)&todo2.list_entry);
	printk(KERN_INFO "address todo2: 0x%llx\n", (unsigned long long)&todo3.list_entry);
	printk(KERN_INFO "address todo3: 0x%llx\n", (unsigned long long)&todo4.list_entry);
	printk(KERN_INFO "address todo4: 0x%llx\n", (unsigned long long)&todo5.list_entry);
	struct list_head *pos = NULL;
	struct todo_struct *item = NULL;
	list_for_each(pos, &todo1.list_entry)
	{
		item = container_of(pos, struct todo_struct, list_entry);
		printk(KERN_INFO "todo: {id: %u, priority: %d}\n", item->id, item->priority);
		printk(KERN_INFO "address: 0x%llx\n", (unsigned long long)pos);
	}
	item = list_entry(pos, struct todo_struct, list_entry);
	printk(KERN_INFO "todo: {id: %u, priority: %d}\n", item->id, item->priority);
	printk(KERN_INFO "address: 0x%llx\n", (unsigned long long)pos);

	
	return 0;
}

void __exit hello_exit(void)
{
	printk(KERN_INFO " hello driver exit \n");
	cdev_del(gDev);
	kfree(gFile);
	kfree(gDev);
	unregister_chrdev_region(devNum, subDevNum);

	free_pages((unsigned long)two_pages, 2);
	kmem_cache_free(pond, p1);
	kmem_cache_destroy(pond);

	destroy_workqueue(my_workqueue);

	return;
}

module_init(hello_init);
module_exit(hello_exit);
MODULE_LICENSE("GPL");

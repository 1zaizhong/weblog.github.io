/*
 Navicat MySQL Data Transfer

 Source Server         : 本地测试
 Source Server Type    : MySQL
 Source Server Version : 50719 (5.7.19)
 Source Host           : localhost:3306
 Source Schema         : weblog

 Target Server Type    : MySQL
 Target Server Version : 50719 (5.7.19)
 File Encoding         : 65001

 Date: 15/12/2025 01:26:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `cover` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章封面',
  `summary` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '文章摘要',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标志位：0：未删除 1：已删除',
  `read_num` int(11) UNSIGNED NOT NULL DEFAULT 1 COMMENT '被阅读次数',
  `type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '文章类型 - 1：普通文章，2：收录于知识库',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO `t_article` VALUES (7, '标题<测试版>', 'https://img.quanxiaoha.com/quanxiaoha/193dd1504ebb4f138085acb23619e0dd.jpg', '无简介,无', '2025-05-14 22:57:19', '2025-05-15 19:44:34', 0, 1, 1);
INSERT INTO `t_article` VALUES (8, '没有标题', 'http://127.0.0.1:9000/weblog/212b1597376b4037af13207ed4663147.jpg', '', '2025-05-15 23:17:06', '2025-05-15 23:17:06', 0, 1, 1);
INSERT INTO `t_article` VALUES (9, '哈哈', 'http://127.0.0.1:9000/weblog/9bed3f472bb6497880afd1db8f282419.jpg', '', '2025-05-15 23:17:52', '2025-06-04 17:59:00', 0, 1, 1);
INSERT INTO `t_article` VALUES (10, 'Git版本管理', 'http://127.0.0.1:9000/weblog/01ddc1b20e4646118b64896669988569.png', '', '2025-06-16 23:06:32', '2025-06-16 23:07:31', 0, 1, 1);
INSERT INTO `t_article` VALUES (11, 'test1', 'http://127.0.0.1:9000/weblog/c9b985ba78454cb787685f3b00f2ba4a.png', '', '2025-06-16 23:08:23', '2025-06-16 23:08:23', 0, 1, 1);
INSERT INTO `t_article` VALUES (12, 'git管理工具', 'http://127.0.0.1:9000/weblog/5aeef500fb234e79851ba01534ea9336.png', 'git管理,小白从0开始理解', '2025-06-16 23:13:55', '2025-12-13 17:41:43', 0, 1, 1);
INSERT INTO `t_article` VALUES (13, '测试标题`', 'http://127.0.0.1:9000/weblog/4dbd77c2c60b4ca39d846df082ca1a8b.png', '', '2025-06-17 08:43:49', '2025-06-17 08:45:48', 0, 1, 1);
INSERT INTO `t_article` VALUES (14, 'test', 'http://127.0.0.1:9000/weblog/9c932d58698e4e6aa5b7e36f319799ae.png', '', '2025-06-17 09:15:54', '2025-06-17 09:15:54', 0, 2, 1);
INSERT INTO `t_article` VALUES (15, '没有标题,这是测试图片内容', 'http://127.0.0.1:9000/weblog/3d899f345b634f6d80ad83df2ac7a3ae.png', '', '2025-09-29 17:51:07', '2025-10-17 16:36:24', 0, 2, 1);
INSERT INTO `t_article` VALUES (16, 'Linux配置环境', 'http://127.0.0.1:9000/weblog/7df118e673974f43b2c65933132228bf.png', 'Linux配置mysql等服务', '2025-12-13 18:18:24', '2025-12-13 19:39:54', 0, 14, 1);
INSERT INTO `t_article` VALUES (17, '测试', 'http://127.0.0.1:9000/weblog/29d2a1f3bf5541a99d2c295d4631cbab.png', '', '2025-12-13 19:35:56', '2025-12-13 19:35:56', 0, 13, 1);

-- ----------------------------
-- Table structure for t_article_category_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_article_category_rel`;
CREATE TABLE `t_article_category_rel`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `article_id` bigint(20) UNSIGNED NOT NULL COMMENT '文章id',
  `category_id` bigint(20) UNSIGNED NOT NULL COMMENT '分类id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_article_id`(`article_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章所属分类关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article_category_rel
-- ----------------------------
INSERT INTO `t_article_category_rel` VALUES (7, 7, 45);
INSERT INTO `t_article_category_rel` VALUES (8, 8, 8);
INSERT INTO `t_article_category_rel` VALUES (12, 9, 33);
INSERT INTO `t_article_category_rel` VALUES (14, 10, 50);
INSERT INTO `t_article_category_rel` VALUES (15, 11, 48);
INSERT INTO `t_article_category_rel` VALUES (19, 13, 48);
INSERT INTO `t_article_category_rel` VALUES (20, 14, 50);
INSERT INTO `t_article_category_rel` VALUES (25, 15, 3);
INSERT INTO `t_article_category_rel` VALUES (26, 12, 50);
INSERT INTO `t_article_category_rel` VALUES (33, 17, 50);
INSERT INTO `t_article_category_rel` VALUES (35, 16, 45);

-- ----------------------------
-- Table structure for t_article_content
-- ----------------------------
DROP TABLE IF EXISTS `t_article_content`;
CREATE TABLE `t_article_content`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章内容id',
  `article_id` bigint(20) NOT NULL COMMENT '文章id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '教程正文',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章内容表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article_content
-- ----------------------------
INSERT INTO `t_article_content` VALUES (7, 7, '这是内容');
INSERT INTO `t_article_content` VALUES (8, 8, '全是内容');
INSERT INTO `t_article_content` VALUES (9, 9, '哈哈');
INSERT INTO `t_article_content` VALUES (10, 10, '​\n首先在Git/Github来生成一个仓库,\n\n并复制你的SSH地址:\n\n\n\n然后到你的父工程目录里面,进行git的初始化,并且去绑定你的仓库:\n\n\n\n然后你的文件就可以看到.git文件\n\n\n\noK了,现在绑定git remote add origin (链接)\n\n\n\n验证是否成功: git remote -v\n\n\n\n我们的IDEA也可以看到他已经配好啦\n\n\n\n发现没,已经绑定了,那现在绑定了,是不是就可以推送了呢?非也,你想想,既然你可以绑定,那别人看你代码是不是也有你的url?难道别人也可以推送拉取?那你不是毁了吗兄弟,\n\n所以现在SSH就来了,git 生成一个公钥\n\nssh-keygen -t rsa -C \"你的邮箱\"\n\n就会出来这一段:\n\n\n\n然后复制你的:id_rsa.pub 文件内容\n\n当然你也可以 cat  //~/d_rsa.pub\n\n\n\n遇事不慌,先测试一手:ssh -T git@gitee.com\n\n发现我们的Gitee认识我们啦\n\n\n\n哎?公钥就出来了,我们拿到了,去哪配呢?肯定是Gitee啦,\n\nGitee里面有一个公钥管理\n\n\n\n他也给你提示嘞:\n\n然后咱们就去配置公钥,名字就自己定啦\n\n\n\n配置好了就可以推送啦:在IDEA里面,选择提交\n\n\n\n然后他就显示我们的代码,从本地,提交到Gitee上面啦,恭喜你,又强了一步\n\n​');
INSERT INTO `t_article_content` VALUES (11, 11, '请输入内容');
INSERT INTO `t_article_content` VALUES (12, 12, '​\nGit的学习是不依赖我们前面学习的知识，就算没有学习java也可以学习\n\nGit就是一个类似于百度云盘的仓库\n\n重点是要掌握使用idea操作Git，企业用的最多，一般不会去使用命令\n\nGit通过不断阶段保存文件来实现版本控制，比如备份，版本还原等等\n\n每修改一次就认为是一个版本，这个版本是谁写的，我们加上版本号\n\ngit记录了开发的全过程，谁在什么时间做了什么事情都可以看得很清楚\n\ngit开发到一定阶段就可以设置一个标签并设置当前代码开发的版本，就像软件的版本一样，设置了标签，当前标签里面的内容就不会变化了，你提交的代码是不会影响到标签的\n\n文章目录\n1.目标\n2、概述\n2.1、开发中的实际场景\n2.2、版本控制器的方式\n2.3、SVN\n2.4 Git\n2.5 Git工作流程图\n3、Git安装与常用命令\n3.1、Git环境配置\n3.1.1下载与安装\n3.1.2 基本配置\n3.1.3 为常用指令配置别名（可选）\n3.1.4 解决GitBash乱码问题\n3.2、获取本地仓库\n3.3、基础操作指令\n3.3.1、查看修改的状态（status）\n3.3.2、添加工作区到暂存区(add)\n3.3.3、提交暂存区到本地仓库(commit)\n3.3.4、查看提交日志(log)\n3.3.5、版本回退\n3.3.6、添加文件至忽略列表\n练习、基础操作\n3.4、分支\n3.4.1、查看本地分支\n3.4.2、创建本地分支\n3.4.4、切换分支(checkout)\n3.4.6、合并分支merge)\n3.4.7、删除分支\n3.4.8、解决冲突\n3.4.9、开发中分支使用原则与流程\n练习：分支操作\n4、Git远程仓库\n4.1、 常用的托管服务[远程仓库]\n4.2、 注册码云\n4.3、 创建远程仓库\n4.4、配置SSH公钥\n4.5、操作远程仓库\n4.5.1、添加远程仓库\n4.5.2、查看远程仓库\n4.5.3、推送到远程仓库\n4.5.4、 本地分支与远程分支的关联关系\n4.5.5、从远程仓库克隆\n4.5.6、从远程仓库中抓取和拉取\n4.5.7、解决合并冲突\n练习:远程仓库操作\n5、在Idea中使用Git（重点掌握）\n5.1、在Idea中配置Git\n5.2、在Idea中操作Git\n5.2.1、创建项目远程仓库（参照4.3）\n5.2.2、初始化本地仓库（将当前项目初始化为仓库）\n5.2.3、设置远程仓库\n5.2.4、提交到本地仓库\n5.2.6、推送到远程仓库\n5.2.7、克隆远程仓库到本地\n5.2.8、创建分支\n5.2.9、切换分支及其他分支相关操作\n5.2.11、解决冲突\n5.2.12查看分支\n5.2.13创建分支\n5.2.14合并分支\n5.3、IDEA常用GIT操作入口\n5.4、场景分析\n附:几条铁令\n附:疑难问题解决\nwindows下看不到隐藏的文件（.bashrc、.gitignore）\n**windows下无法创建.ignore|.bashrc文件**\n附：IDEA集成GitBash作为Terminal\nGit的优势\n分支模型\n总结\n1.目标\n\n\n\n\n\n2、概述\n2.1、开发中的实际场景\n场景一：备份 小明负责的模块就要完成了，就在即将Release之前的一瞬间，电脑突然蓝屏，硬盘光荣牺牲！几个月 来的努力付之东流 \n\n场景二：代码还原 这个项目中需要一个很复杂的功能，老王摸索了一个星期终于有眉目了，可是这被改得面目全非的 代码已经回不到从前了。什么地方能买到哆啦A梦的时光机啊？ \n\n场景三：协同开发 小刚和小强先后从文件服务器上下载了同一个文件：Analysis.java。小刚在Analysis.java 文件中的第30行声明了一个方法，叫count()，先保存到了文件服务器上；小强在Analysis.java文件中的 第50行声明了一个方法，叫sum()，也随后保存到了文件服务器上，于是，count()方法就只存在于小刚的记 忆中了 \n\n场景四：追溯问题代码的编写人和编写时间！ 老王是另一位项目经理，每次因为项目进度挨骂之后，他都不知道该扣哪个程序员的工资！就拿这 次来说吧，有个Bug调试了30多个小时才知道是因为相关属性没有在应用初始化时赋值！可是二胖、王东、刘 流和正经牛都不承认是自己干的！\n\n2.2、版本控制器的方式\na、集中式版本控制工具 \n集中式版本控制工具，版本库是集中存放在中央服务器的，team里每个人work时从中央服务器下载代码，是必须联网才能工作，局域网或互联网。个人修改后然后提交到中央版本库。 举例：SVN和CVS \nb、分布式版本控制工具 \n分布式版本控制系统没有“中央服务器”，每个人的电脑上都是一个完整的版本库，这样工作的时候，无需联网了，因为版本库就在你自己的电脑上。多人协作只需要各自的修改推送给对方，就能互相看到对方的 修改了。举例：Git\n\na、集中式版本控制工具\n集中式版本控制工具，版本库是集中存放在中央服务器的，team里每个人work时从中央服务器下载代码，是必须联网才能工作，局域网或互联网。个人修改后然后提交到中央版本库。 举例：SVN和CVS\nb、分布式版本控制工具\n分布式版本控制系统没有“中央服务器”，每个人的电脑上都是一个完整的版本库，这样工作的时候，无需联网了，因为版本库就在你自己的电脑上。多人协作只需要各自的修改推送给对方，就能互相看到对方的 修改了。举例：Git\n\n集中式版本控制的缺点：\n\n需要联网，中央服务器磁盘损坏，项目会彻底崩溃\n\n2.3、SVN\nsvn是早期大多数公司在用的版本控制器\n\n2.4 Git\nGit是分布式的,Git不需要有中心服务器，我们每台电脑拥有的东西都是一样的。我们使用Git并且有个 中心服务器，仅仅是为了方便交换大家的修改，但是这个服务器的地位和我们每个人的PC是一样的。我们可以 把它当做一个开发者的pc就可以就是为了大家代码容易交流不关机用的。没有它大家一样可以工作，只不 过“交换”修改不方便而已。 \ngit是一个开源的分布式版本控制系统，可以有效、高速地处理从很小到非常大的项目版本管理。Git是 Linus Torvalds 为了帮助管理 Linux 内核开发而开发的一个开放源码的版本控制软件。 同生活中的许多伟大事物一样，Git 诞生于一个极富纷争大举创新的年代。Linux 内核开源项目有着为数众 多的参与者。 绝大多数的 Linux 内核维护工作都花在了提交补丁和保存归档的繁琐事务上（1991－2002 年间）。 到 2002 年，整个项目组开始启用一个专有的分布式版本控制系统 BitKeeper 来管理和维护代 码。到了 2005 年，开发 BitKeeper 的商业公司同 Linux 内核开源社区的合作关系结束，他们收回了 Linux 内核社区免费使用 BitKeeper 的权力。 这就迫使 Linux 开源社区（特别是 Linux 的缔造者 Linus Torvalds）基于使用 BitKeeper 时的经验教训，开发出自己的版本系统。 \n他们对新的系统制订 了若干目标： \n速度简单的设计 \n对非线性开发模式的强力支持（允许成千上万个并行开发的分支） \n完全分布式 \n有能力高效管理类似 Linux 内核一样的超大规模项目（速度和数据量）\n\n\n\n重点\n\nGit是一个分布式版本管理系统，他通过共享版本库来共享版本信息，所以相当于每个开发人员的本地都有一个共享版本库的拷贝，所有人员的本地版本库和共享版本库都是同步的，所以不用担心共享版本库宕机的问题，只要拿一个开发人员的本地版本库传到共享版本库就好了，开发人员之间可以直接交换版本信息，但是这种方式不常用，我们一般还是通过共享版本库实现共享，这样所有的人员都可以共享到版本信息，无需联网了，因为版本库就在你自己的电脑上，但是要实现版本共享的时候还是需要联网的，自己开发的时候不需要联网。\n\n2.5 Git工作流程图\n\n\n命令如下：\n\n\n\n\n\n\n\n\n3、Git安装与常用命令\n本教程里的git命令例子都是在Git Bash中演示的，会用到一些基本的linux命令，在此为大家提前列举：\n\n\n\n\n\n3.1、Git环境配置\n3.1.1下载与安装\n下载地址： https://git-scm.com/download\n\n\n\n下载完成后可以得到如下安装文件：\n\n\n\n双击下载的安装文件来安装Git。安装完成后在电脑桌面（也可以是其他目录）点击右键，如果能够看\n\n到如下两个菜单则说明Git安装成功。\n\n\n\n备注：\n\nGit GUI：Git提供的图形界面工具\n\nGit Bash：Git提供的命令行工具\n\n当安装Git后首先要做的事情是设置用户名称和email地址。这是非常重要的，因为每次Git提交都会使用该用户信息\n\nGit版本控制要记录哪个人什么时候做了什么事情，Git就是通过邮箱去辨识是哪个人的\n\n3.1.2 基本配置\n\n\ngit config --global user.name “syc”\n\ngit config --global user.email “227.qq.com”\n\n\n\n邮箱可以是假邮箱\n\n查看配置信息\n\ngit config --global user.name\n\ngit config --global user.email\n\n3.1.3 为常用指令配置别名（可选）\n有些常用的指令参数非常多，每次都要输入好多参数，我们可以使用别名。\n\n打开用户目录，创建 .bashrc 文件\n部分windows系统不允许用户创建点号开头的文件，可以打开gitBash,执行 touch ~/.bashrc\n\n\n\n在 .bashrc 文件中输入如下内容：\n#用于输出git提交日志 \nalias git-log=\'git log --pretty=oneline --all --graph --abbrev-commit\' \n#用于输出当前目录所有文件及基本信息 \nalias ll=\'ls -al\'\n\n \n\n打开gitBash，执行 source ~/.bashrc\n\n\n3.1.4 解决GitBash乱码问题\n打开GitBash执行下面命令\ngit config --global core.quotepath false\n1\n${git_home}/etc/bash.bashrc 文件最后加入下面两行\nexport LANG=\"zh_CN.UTF-8\" \nexport LC_ALL=\"zh_CN.UTF-8\"\n\n1\n2\n3.2、获取本地仓库\n要使用Git对我们的代码进行版本控制，首先需要获得本地仓库\n\n1）在电脑的任意位置创建一个空目录（例如test）作为我们的本地Git仓库\n\n2）进入这个目录中，点击右键打开Git bash窗口\n\n3）执行命令git init\n\n4）如果创建成功后可在文件夹下看到隐藏的.git目录。\n\n\n\n3.3、基础操作指令\nGit工作目录下对于文件的修改(增加、删除、更新)会存在几个状态，这些修改的状态会随着我们执行Git\n\n的命令而发生变化。\n\n\n\n工作区，就是平时存放项目代码的地方。\n\n本章节主要讲解如何使用命令来控制这些状态之间的转换：\n\n\n\n3.3.1、查看修改的状态（status）\n\n\n\n\n新创建的文件是未跟踪状态\n\n\n\n即将被提交的意思\n\n\n\n提交完后显示缓冲区没有东西可以提交了\n\n新建文件是显示new file 修改文件就是实现modified:\n\n3.3.2、添加工作区到暂存区(add)\n\n\n将所有修改加入暂存区：git add .\n3.3.3、提交暂存区到本地仓库(commit)\n\n\n\n\n\n提交时候添加的备注会被放到日志中\n\n提交完后显示缓冲区没有东西可以提交了\n\n3.3.4、查看提交日志(log)\n在3.1.3中配置的别名 git-log 就包含了这些参数，所以后续可以直接使用指令 git-log\n\n\n\n\n\n\n\n\n查看log我们一般都是会加上上面全部的参数的，这样显示更美观有序，我们在上面给这个指令设置了别名\n\n#用于输出git提交日志 \nalias git-log=\'git log --pretty=oneline --all --graph --abbrev-commit\' \n\n1\n2\n我们只要使用git -log命令就好了 ，简单命令为git log\n\n\n\n提交时候添加的备注会被放到日志中\n\n3.3.5、版本回退\n撤回到之前的某个操作，他回去删除我们撤回到位置之后的版本\n\n\n\ncommitID 可以使用 git-log 或 git log 指令查看\n\n\n\n我们可以在reflog里面知道删除文件的id，我们可以直接使用命令git reset --hard commitID 还原\n\n所以\n\ngit reset --hard commitID既可以做版本回退，也可以做版本还原\n\n3.3.6、添加文件至忽略列表\n一般我们总会有些文件无需纳入Git 的管理，也不希望它们总出现在未跟踪文件列表。 通常都是些自动\n\n生成的文件，比如日志文件，或者编译过程中创建的临时文件等。 在这种情况下，我们可以在工作目录\n\n中创建一个名为 .gitignore 的文件（文件名称固定），列出要忽略的文件模式。下面是一个示例：\n\n\n\n\n\n这样后缀为.a的文件就不会被加到缓冲区中，这样git就不会去处理这些文件了\n\n一般.gitignore文件公司会给\n\n\n\n练习、基础操作\n①创建文件提交\n\n②修改文件提交\n\n\n\n\n\n–hard放在id前面或者后面都可以\n\n3.4、分支\n几乎所有的版本控制系统都以某种形式支持分支。 使用分支意味着你可以把你的工作从开发主线上分离\n\n开来进行重大的Bug修改、开发新的功能，以免影响开发主线。master是我们的主线\n\n每个人开发的那一部分就是一个分支，使得每个人的开发互不影响，在每个人都开发完后就将所有的代码汇总到一起，此时就要执行分支的合并操作\n\n工作区只能在一个分支工作，每个分支存放的文件或者资源是不一样的，就相当于不同的文件夹\n\nmaster分支\n\n\n\ntest01分支\n\n只是我们只能看到当前分支的内容\n\n\n\nhead指向谁，谁就是当前分支\n\n3.4.1、查看本地分支\n命令：git branch\n\n\n*号表示所在的分支\n\n3.4.2、创建本地分支\n命令：git branch 分支名\n创建的新分支会建立在当前分支的版本之上，所以新建的分支会有当前分支的内容\n3.4.4、切换分支(checkout)\n命令：git checkout 分支名\n我们还可以直接切换到一个不存在的分支（创建并切换）\n\n命令：git checkout -b 分支名\n3.4.6、合并分支merge)\n注意：分支上的内容必须先提交,才能切换分支\n\n一个分支上的提交可以合并到另一个分支\n\n命令：git merge 分支名称\n在每个人都开发完后就将所有的代码汇总到一起，此时就要执行分支的合并操作\nmaster使我们的主线，我们一般将分支合并到主线上面去\n\n步骤：切换到master分支，然后执行合并命令，执行完后，分支上的资源、文件就会被合并到主线上面去\n\n\n\n第三行是master分支创建了ignore文件，然后第二行执行dev01分支合并到master分支的操作，最后head指向了主分支\n\n例如将test01分支合并到master分支上，master分支上就会加上test01分支上的内容\n\n\n\n\n分支岔开表示有多个分支在修改同一个文件\n\n3.4.7、删除分支\n不能删除当前分支，只能删除其他分支\n\n\n\n小d删除了就使用D，一般使用d就够了\n\n我们去删除没有合并的分支的时候就会出现删除不了的情况，此时就可以使用D\n\n3.4.8、解决冲突\n当我们合并分支后，两个或者多个分支对同一个文件的同一个地方进行修改的时候（不是同一个地方是不会出现冲突的 ），此时git就不知道要取哪个分支修改的值，是取a分支修改的值，还是取b分支修改的值呢，此时就产生了冲突\n\n冲突的报错\n\n\n\n此时的文件样子\n\n\n\n第一个count值表示的是当前分支修改的值\n\n第二个count值是在dev分支修改的值\n\n当两个分支上对文件的修改可能会存在冲突，例如同时修改了同一个文件的同一行，这时就需要手动解\n\n决冲突，解决冲突步骤如下：\n\n其实我们就是直接手动去删除文件中的一个分支，留下一个分支，这样就不会冲突了\n\n\n\n\n冲突部分的内容处理如下所示：\n\n\n\n3.4.9、开发中分支使用原则与流程\n几乎所有的版本控制系统都以某种形式支持分支。 使用分支意味着你可以把你的工作从开发主线上分离\n\n开来进行重大的Bug修改、开发新的功能，以免影响开发主线。\n\n在开发中，一般有如下分支使用原则与流程：\n\nmaster （生产） 分支\n​ 线上分支，主分支，中小规模项目作为线上运行的应用对应的分支；\n\ndevelop（开发）分支\n​ 是从master创建的分支，一般作为开发部门的主要开发分支，如果没有其他并行开发不同期上线\n\n​ 要求，都可以在此版本进行开发，阶段开发完成后，需要是合并到master分支,准备上线。\n\n​ 例如我们要开发新功能，我们要可以在develop分支上在建一个分支，新功能一般叫做feature分支，开发完以后在合并到 develop分支上面去，而不是直接提交到master分支，最后项目做完了develop在合并到master分支上\n\ndevelop和master分支是不可删除的\n\nfeature/xxxx分支（用完可删）\n​ 从develop创建的分支，一般是同期并行开发，但不同期上线时创建的分支，分支上的研发任务完\n\n​ 成后合并到develop分支，用完后可删除。\n\nhotfifix/xxxx分支，\n​ 从master派生的分支，一般作为线上bug修复使用，修复测试完成后需要合并到master、test、develop分支。\n\n还有一些其他分支，在此不再详述，例如test分支（用于代码测试）、pre分支（预上线分支）等等。\n\n\n练习：分支操作\n\n\n4、Git远程仓库\n4.1、 常用的托管服务[远程仓库]\n前面我们已经知道了Git中存在两种类型的仓库，即本地仓库和远程仓库。那么我们如何搭建Git远程仓库 呢？我们可以借助互联网上提供的一些代码托管服务来实现，其中比较常用的有GitHub、码云、GitLab等。 \ngitHub（ 地址：https://github.com/ ）是一个面向开源及私有软件项目的托管平台，因为只支持 Git 作为唯一的版本库格式进行托管，故名gitHub \n\n码云（地址： https://gitee.com/ ）是国内的一个代码托管平台，由于服务器在国内，所以相比于 GitHub，码云速度会更快 \n\nGitLab （地址： https://about.gitlab.com/ ）是一个用于仓库管理系统的开源项目，使用Git作 为代码管理工具，并在此基础上搭建起来的web服务,一般用于在企业、学校等内部网络搭建git私服。\n1\n2\n3\n4\n5\n6\n企业里面我们一般使用GitLab,毕竟代码放在自己的机房里面才安全，gitLab要自己部署\n\n4.2、 注册码云\n要想使用码云的相关服务，需要注册账号（地址： https://gitee.com/signup ）\n\n\n\n4.3、 创建远程仓库\n\n\n仓库创建完成后可以看到仓库地址，如下图所示:\n\n\n\n4.4、配置SSH公钥\n\nssh-keygen -t rsa\n不断回车\n如果公钥已经存在，则自动覆盖\n\n获取公钥\ncat ~/.ssh/id_rsa.pub\n\n\n验证是否配置成功\nssh -T git@gitee.com\n4.5、操作远程仓库\n4.5.1、添加远程仓库\n此操作是先初始化本地库，然后与已创建的远程库进行对接。\n\n我们要将本地仓库和远程仓库连接起来，一般一个本地仓库对应一个远程仓库远侧，远程仓库默认名为origin\n\n\n\n\n\n例如: git remote add origin git@gitee.com:czbk_zhang_meng/git_test.git\n\n4.5.2、查看远程仓库\n命令：git remote\n\n\n4.5.3、推送到远程仓库\n\n\n\ngit push origin master 这里表示将本地仓库当前master分支的内容推到远程仓库上面去\n\n\ngit push --set-upstream origin master\n\n\ngit push 将master分支推送到已关联的远端分支。\n\n\n\n查询远程仓库\n\n\n\n4.5.4、 本地分支与远程分支的关联关系\n查看关联关系我们可以使用 git branch -vv 命令\n\n\n4.5.5、从远程仓库克隆\n如果已经有一个远端仓库，我们可以直接clone到本地。\n\n命令: git clone <仓库路径> [本地目录]\n本地目录可以省略，会自动生成一个目录，就是SSH后面那部分\n\n\n\n\n左边是我们上传的仓库，右边是克隆下来的仓库\n\n不同的主机都把修改完了版本资源放在远程仓库上，然后其他人都是克隆，这样就可以实现不同主机之间的数据同步了，数据都是一样的\n\n克隆一般只会执行一次，就是在你进去公司的时候，别人提交了以后，我们不需要去克隆整个仓库，仓库是很大的，克隆也需要花很多时间，所以要去远程仓库中抓取我们要的版本信息，就是那些别人新增加的提交\n\n4.5.6、从远程仓库中抓取和拉取\n远程分支和本地的分支一样，我们可以进行merge操作，只是需要先把远端仓库里的更新都下载到本地，再进行操作。\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n发现此时本地的修改和远程仓库的修改同步了\n\n在test01这个本地仓库进行一次提交并推送到远程仓库\n\n\n在另一个仓库将远程提交的代码拉取到本地仓库\n\n\n4.5.7、解决合并冲突\n我们要更新远程仓库的资源时，先要获取此时远程仓库的资源后，在合并到自己的master分支中，然后再上传到远程仓库上\n\n在一段时间，A、B用户修改了同一个文件，且修改了同一行位置的代码，此时会发生合并冲突。\n\nA用户在本地修改代码后优先推送到远程仓库，此时B用户在本地修订代码，提交到本地仓库后，也需要\n\n推送到远程仓库，此时B用户晚于A用户，故需要先拉取远程仓库的提交，经过合并后才能推送到远端分\n\n支,如下图所示。\n\n\n\n在B用户拉取代码时，因为A、B用户同一段时间修改了同一个文件的相同位置代码，故会发生合并冲\n\n突。\n\n就是b在更新一个资源之前，有一个a在b之前率先改掉了这个资源，此时就会出现分支冲突的问题，git不知道是要取a修改的值，还是b修改的值，此时就要我们手动去对应文件里去修改，到底要保留哪一个\n\n这不是一个先提交，一个后提交吗，拿到的前一个提交的版本信息，为什么还会冲突呢，为什么不能直接修改count的值呢，这个同一时间有点抽象，？？？？？？？？？？这里不理解\n\n远程分支也是分支，所以合并时冲突的解决方式也和解决本地分支冲突相同相同，在此不再赘述，需要\n\n学员自己练习。\n\n练习:远程仓库操作\n\n\n5、在Idea中使用Git（重点掌握）\n5.1、在Idea中配置Git\n安装好IntelliJ IDEA后，如果Git安装在默认路径下，那么idea会自动找到git的位置，如果更改了Git的安\n\n装位置则需要手动配置下Git的路径。选择File→Settings打开设置窗口，找到Version Control下的git选\n\n项：\n\n\n\n点击Test按钮,现在执行成功，配置完成\n\n\n\n5.2、在Idea中操作Git\n场景：本地已经有一个项目，但是并不是git项目，我们需要将这个放到码云的仓库里，和其他开发人员\n\n继续一起协作开发。\n\n5.2.1、创建项目远程仓库（参照4.3）\n\n\n5.2.2、初始化本地仓库（将当前项目初始化为仓库）\n在idea中创建文件的时候，它会询问你是否要将文件添加到git中，修改文件的时候，idea会自动帮我们去add，我们只需要去commit就好了\n\n\n\n此时我们的项目目录就变成了一个本地仓库\n\n\n\n绿色的文件代表已添加到git中\n\n爆红的文件没有被添加到git当中，被Git识别为冲突文件\n\n灰色的文件代表已忽略的文件，可以在gitignore文件中配置\n\n\n\n\n\n这里只是我们的本地仓库\n\n5.2.3、设置远程仓库\n远程仓库名默认为origin\n\n\n5.2.4、提交到本地仓库\n\n\n5.2.6、推送到远程仓库\n在将本地仓库的修改推送到git远程仓库的时候，我们要先pull，先拿到此时远程仓库的版本信息，再去此版本信息上修改\n\n\n\n5.2.7、克隆远程仓库到本地\n\n\n5.2.8、创建分支\n\n\n\n\n5.2.9、切换分支及其他分支相关操作\n\n\n5.2.11、解决冲突\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n5.2.12查看分支\n\n\n\n可以看到本地仓库的分支和远程仓库的分支\n\n5.2.13创建分支\n方式一：\n\n\n方式二：（推荐）\n\n\n\n这种方式还可以看到分支所在的版本信息\n\n5.2.14合并分支\n\n\n这个标签表示是当前分支\n\n我们到想要合并的分支上面，右键\n\n\n5.3、IDEA常用GIT操作入口\n\n\n\n\n5.4、场景分析\n基于我们后面的实战模式，我们做一个综合练习\n\n当前的开发环境如下，我们每个人都对这个项目已经开发一段时间，接下来我们要切换成团队开发模\n\n式。\n\n也就是我们由一个团队来完成这个项目实战的内容。团队有组长和若干组员组成（组长就是开发中的项\n\n目经理）。\n\n所有操作都在idea中完成。\n\n练习场景如下：\n\n1、由组长，基于本项目创建本地仓库；创建远程仓库，推送项目到远程仓库。\n\n\n2、每一位组员从远程仓库克隆项目到idea中,这样每位同学在自己电脑上就有了一个工作副本，可以正\n\n式的开始开发了。我们模拟两个组员(组员A、组员B)，克隆两个工作区。\n\n\n3、组员A修改工作区,提交到本地仓库，再推送到远程仓库。组员B可以直接从远程仓库获取最新的代\n\n码。\n\n\n4、组员A和组员B修改了同一个文件的同一行，提交到本地没有问题，但是推送到远程仓库时，后一个\n\n推送操作就会失败。\n\n解决方法：需要先获取远程仓库的代码到本地仓库，编辑冲突，提交并推送代码。\n\n\n\n附:几条铁令\n\n\n\n附:疑难问题解决\nwindows下看不到隐藏的文件（.bashrc、.gitignore）\n\n\nwindows下无法创建.ignore|.bashrc文件\n这里以创建 .ignore 文件为例：\n\n\n\n\n\n附：IDEA集成GitBash作为Terminal\n\n\nGit的优势\n\n\n查看项目实现的步骤\n\n\n\n分支模型\n相当于老板或者组长给你发布任务\n\n组长将原始项目（项目框架）发到Git共享版本库中，组员就去Git上将代码更新到本地\n\n对于分支的使用，我们每天可以创建一个分支来存放今天工作的代码，然后完成工作后就把每天分支上的代码合并到master上，然后上传到共享版本库中，公司使用的是git-lab\n\n\n\n\n\n每一个讲义代码都是事准备好的，他包括了前面代码的总和\n\n总结\n\n\nidea操作Git有很多中方式，我们选择自己喜欢的一种就好了\n\n​');
INSERT INTO `t_article_content` VALUES (13, 13, '无内容');
INSERT INTO `t_article_content` VALUES (14, 14, '请输入内容');
INSERT INTO `t_article_content` VALUES (15, 15, '![](http://127.0.0.1:9000/weblog/09446e10b7834c5081ee52d53b90f0de.png)\n![](http://127.0.0.1:9000/weblog/3a7ba9c994994caba9e7f749597c3053.png)\n');
INSERT INTO `t_article_content` VALUES (16, 16, '安装方式：(红色是常用）\n二进制发布包安装软件已经针对具体平台编译打包发布，只要解压，修改配置即可（别人发给你你自己安装））\nrpm安装软件已经按照redhat的包管理规范进行打包，使用rpm命令进行安装，不能自行解决库依赖问题\nyum：安装一种在线软件安装方式，本质上还是rpm安装，自动下载安装包安装，安装过程中自动解决库依赖问题（拉取镜像这种）\n源码编译安装软件以源码工程的形式发布，需要自己编译打包\n配置jdk:\n这里用的是老师给的17，直接拉包进Moba ,然后找压缩包所在的目录，解压到直接建的目录里面。\ntar -zxvf jdk-17.0.12_linux-x64_bin.tar.gz -C /usr/local/src/javaTools/jdk17\n然后再去找配置文件：/etc/profile文件\n添加路径：\nJAVA_HOME=/usr/local/src/javaTools/jdk17/jdk-17.0.12/\nPATH=$JAVA_HOME/bin:$PATH\n再然后重新加载文件：\n重新加载profile文件，使更改的配置立即生效，进入etc目录。命令为source/etc/profile、检查安装是否成功，命令为java-version\n\n\n\n安装Tomcat：\n根jdk一样解压，然后直接可以启动，不用配置\n./startup.sh //启动\n./shutdown.sh     // 关闭\n浏览器访问：ip :8080 就能看到，如果启动不能看到，记得去豆包弄一下端口\n是否启动也可以通过：进bin 然后： ps -ef  |  grep tomcat  看一下进程\n看日志： tail -20  /usr/local/src/javaTools/tomcat7/apache-tomcat-9.0.98/logs/catalina.out\n\nMySQL5.7的安装：\n去官网下再压缩文件，解压以后解压rpm就行，\n第一次安装记得删掉依赖：yum remove -y mariadb-libs\n然后：tar -xvf xxx\n\n检验： systemctl status mysqld 查看服务，\n           mysql --version 查看版本好\n          systemctl start mysqld 开启服务\n          ps -ef | grep msql    查看进程\nsystemctl enable  mysqld 开机自动启动服务\n netstat -tunlp 查看已经启动的服务\n\n 登录：\ncat /var/log/mysqld.log | grep password 查看临时密码\nmysql -uroot -p临时密码\n\nmysql-uroot–p登录mysql（使用临时密码登录）\n#修改密码set global validate_password_length=4;设置密码长度最低位数\nset global validate_password_policy=LOW;设置密码安全等级低，便于密码可以修改成root\n设置密码为root set password = password(\'root\');\n#开启访问权限 grant all on *.* to \'root\'@\'%\' identified by \'root\';\nflush privileges;\n每一个语句记得带；\n\n防火墙操作：\n查看防火墙状态(systemctl statusfirewalld、firewall-cmd--state)\n暂时关闭防火墙(systemctl stopfirewalld)\n永久关闭防火墙(systemctl disable firewalld)\n开启防火墙(systemctlstartfirewalld)\n开放指定端口(firewall-cmd--zone=public--add-port=8080/tcp--permanent)\nfirewall-cmd --zone=public --add-port=3306/tcp --permanent\n关闭指定端口(firewall-cmd--zone=public--remove-port=8080/tcp--permanent)\n立即生效(firewall-cmd--reload)\n查看开放的端口(firewall-cmd--zone=public--list-ports)\nfirewall-cmd --reload 修改防火墙后记得这个\n');
INSERT INTO `t_article_content` VALUES (17, 17, '## 👋 自我介绍\n\n> 大家好，我是小哈。前某厂中台架构，公众号小哈学 Java 作者。91年生人，码龄 9 年，先后供职于支付、共享等互联网领域，主导负责过数据传输、日志平台、任务调度、文件平台等产品，以支撑各部门业务线。喜欢分享知识，热爱技术，也不止于技术，不只是写 Java，业余也爱玩前端、Python、Android 等，是个活跃的技术折腾者。\n\n本实战项目是星球内部第一个项目，目前**已输出 17w+ 字，更新了 124 小节内容，演示图片：802 张，还在持续爆肝中，目标是将 Java 程序员生涯中，比较典型的项目都教会大家，如前后端分离博客全栈级开发、秒杀系统、在线商城、IM 即时通讯（Netty）、权限管理（Spring Security）、Spring Cloud Alibaba 微服务等等... 目前有 370+ 小伙伴已加入，一起学习打卡，一起进步！同频的人才能走的更快、更远 ！** 欢迎各位小伙伴加入哟~\n\n👉 [加入星球](https://t.zsxq.com/12h3hIayK \"加入星球\")，你将获得: <span style=\"color: #f97316; font-weight: 700;\">专属的项目实战 / Java 学习路线 / 一对一提问 / 学习打卡 / 赠书活动</span>，与一群热爱学习的小伙伴一起，将走的更快、更远! **公众号：小哈学Java, 回复【星球】**，可领取<span style=\"color: #f97316; font-weight: 700;\">专属优惠券</span>~\n\n---\n\n> 本项目 1.0 版本已部署到了阿里云服务器上，可点击下面链接进行访问，查看实际效果：\n>\n> 演示地址：[http://118.31.41.16/](http://118.31.41.16/)\n>\n> 后台登录演示账号:\n> \n> - 账号：test\n> - 密码：test\n\n## 🏃 关于实战项目\n\n知识星球是个私密学习圈子，我会在星球内部，出**一系列从 0 到 1 的实战项目，贴合真实的企业级项目开发规范，使用主流的企业技术栈，全程手写后端 + 前端完整代码，通过专栏的形式，把每个功能点的开发的步骤，手摸手，通过丰富的图片 + 文字，保姆级教学（PS: 同时按小节进度提供代码，不至于一上来代码量太多，不知道从哪入手）**。\n\n\n![](https://img.quanxiaoha.com/quanxiaoha/169361945065538)\n\n目前，我已经给自己的网站：[犬小哈教程](www.quanxiaoha.com/column \"犬小哈教程\") 新开发了专栏模块，可以让小伙伴们只需跟着实战专栏，按照章节顺序教学来，上手敲，即可搞定每个功能点的开发，成体系地完成一个独立项目。*目前加入的小伙伴，都给出了超高评价，以下了截取了部分好评*：\n\n![](https://img.quanxiaoha.com/quanxiaoha/169733756405612)\n\n![](https://img.quanxiaoha.com/quanxiaoha/169733761293187)\n\n![](https://img.quanxiaoha.com/quanxiaoha/169733762195775)\n\n另外，在跟随小节内容上手的过程中，若遇到问题，可在星球内发起 *1v1 提问，小哈亲自解答*。\n\n![\"星球内提问\"](https://img.quanxiaoha.com/quanxiaoha/169396126861858 \"星球内提问\")\n\n星球说不清楚的，项目进度因为某一块搞不定的，微信发我源码，帮忙看问题出在哪：\n\n![搞不定的，微信发我源码，帮忙看问题出在哪](https://img.quanxiaoha.com/quanxiaoha/169406285385964 \"搞不定的，微信发我源码，帮忙看问题出在哪\")\n\n\n陪伴式写项目，到最终部署到云服务器上，能够通过域名来访问，完成项目上线。\n\n> 💡 TIP : 后期也会尝试分享一些适合程序员的技术副业，比如开发一些小工具网站，进行推广运营，有了一定用户量，能够挣点零花钱啥的。当然，这都是后话了，前提还需要你能够自行完整的开发一个独立应用，前期还是以项目实战为主。\n\n既然小哈是准备出一系列的实战项目，我希望这些项目的难度是循序渐进的，能够让你真实的感受到自己的功力在慢慢增强。但是又不想写那种纯纯的 CRUD 型管理后台项目，太枯燥。那么，第一个项目小哈就定位在难度不大，易上手，有趣，并且非常有代表性，实际工作中也能够被频繁用到的。\n\n脑瓜子一转，想到之前好多读者问我博客的事情，今年 4 月份的时候，又有读者微信问我: *你的博客有没有开源，感觉还挺好看，也想学习、部署一个。*\n\n![](https://img.quanxiaoha.com/quanxiaoha/169355366112215)\n\n于是乎，花了点时间整了第一个实战项目 —— **前后端分离的博客 Weblog**。\n\n\n## 💁 项目介绍\n\n每个技术人都应该有属于自己的博客！相比较直接使用第三方博客平台，自行搭建博客更有成就感；另外就是没有平台限制，比如你想发个二维码引流啥的，平台基本都是不允许的，还有，你可以自由 `div` 定制自己想要的博客 `css` 样式，哪天 UI 看不爽了，咱就自己换；最后，*面试的时候，如果简历贴上的是自己开发博客地址，也会很加分*！\n\n### 🔗 演示地址\n\n目前 1.0 版本已经部署到了阿里云服务器上，可点击下面链接进行访问，查看实际效果：\n\n[http://118.31.41.16/](http://118.31.41.16/ \"http://118.31.41.16/\")\n\n管理后台登录账号/密码:\n\n- 账号：test\n- 密码：test\n\n> ⚠️ 注意：该账号的角色为*游客*角色，*仅支持查询操作*，新增、修改、删除操作会提示不允许。\n\n### ⚒️ 功能模块\n\n> 💡 TIP : 以下*只是 1.0 版本的功能，后续小哈将添加更多功能进去, 比如图库管理、知识库、在线人数统计、SSR（服务端渲染） 等等*，能够想到的高逼格功能，咱都整上，附带超详细的实战图文笔记 ...\n\n![Weblog 功能模块一览](https://img.quanxiaoha.com/quanxiaoha/169560157482464 \"Weblog 功能模块一览\")\n\n### ✏️ 技术栈\n\n![Weblog 技术栈一览](https://img.quanxiaoha.com/quanxiaoha/169560181378937 \"Weblog 技术栈一览\")\n\n## 🎉 专栏目标\n\n学完本项目，你将具备如下能力：\n\n- 掌握独立开发全栈项目的能力（*后端 + 前端*）；\n- 掌握 Spring Boot 相关技术栈，以及构建后端项目能力，写出符合企业级的代码规范；\n- 掌握 Vue 3.2 + Element Plus + Vite 4 技术构建前端工程的能力，并能够手动搭建 Admin 后台管理系统；\n- 掌握前端页面响应式设计（同时适配不同屏幕），排版布局，能够根据自己需求，`div` 自己想要的前端效果；\n- ...\n\n## 💡 专栏亮点\n\n- 在技术选型上，小哈选择了目前主流热门的技术栈，对标企业级项目开发；\n- 严格把控代码质量，数据库设计，写出令同事称道的代码；\n- 熟悉后端工程的搭建，如一些通用的基础设施：参数校验、全局异常捕获、`API` 统一出入参日志打印等等；\n- 能够独立完成整个网站的部署流程，从功能开发到服务器、域名选购，再到网站备案，最终公网可访问；\n- 对象存储 `Minio` 的使用, 能够独立搭建个人图床；\n- 从 0 到 1 ，通过 `Element Plus` 纯手搭 `Admin` 管理后台前端骨架；\n- 使用 Vue 3 `setup` 等语法糖新特性；\n- 博客前台页面在设计上美观大气；\n- ...\n\n## 📖 专栏大纲\n\n整个实战专栏，小哈按功能点开发进度来做的目录，目前已经更新到了第第五章，目录大致如下：\n\n> 💡 TIP : 如下目录不代表最终内容，只会更多，目前只是把已完成的部分详细的罗列了出来，其中大部分功能正在开发中，所属具体小节的标题也会陆续更新进来。\n\n- 一、[项目介绍](https://www.quanxiaoha.com/column/10000.html)\n- 二、开发环境搭建\n  - [2.1 【后端】环境安装&工具准备](https://www.quanxiaoha.com/column/10003.html)\n  - [2.2 【前端】开发环境&工具安装](https://www.quanxiaoha.com/column/10004.html)\n\n- 三、Spring Boot 后端工程搭建\n  - [3.1 搭建 Spring Boot 多模块工程](https://www.quanxiaoha.com/column/10005.html)\n  - [3.2 Spring Boot 多环境配置](https://www.quanxiaoha.com/column/10006.html)\n  - [3.3 配置 Lombok](https://www.quanxiaoha.com/column/10007.html)\n  - [3.4 Spring Boot 整合 Lockback 日志](https://www.quanxiaoha.com/column/10008.html)\n  - [3.5 Spring Boot 自定义注解，实现 API 请求日志切面](https://www.quanxiaoha.com/column/10009.html)\n  - [3.6 Spring Boot 通过 MDC 实现日志跟踪](https://www.quanxiaoha.com/column/10010.html)\n  - [3.7 Spring Boot 实现优雅的参数校验](https://www.quanxiaoha.com/column/10011.html)\n  - [3.8 Spring Boot 自定义响应工具类](https://www.quanxiaoha.com/column/10012.html)\n  - [3.9 Spring Boot 实现全局异常管理](https://www.quanxiaoha.com/column/10013.html)\n  - [3.10 全局异常处理器+参数校验（最佳实践）](https://www.quanxiaoha.com/column/10014.html)\n  - [3.11 整合 Knife4j：提升接口调试效率](https://www.quanxiaoha.com/column/10015.html)\n  - [3.12 自定义 Jackson 序列化、反序列化，支持 Java 8 日期新特性](https://www.quanxiaoha.com/column/10016.html)\n  - [3.13 小结](https://www.quanxiaoha.com/column/10017.html)\n\n- 四、使用 Vue 3 + Vite 4 搭建前端工程\n  - [4.1 Vue 3 环境安装& Weblog 项目搭建](https://www.quanxiaoha.com/column/10018.html)\n  - [4.2 安装 VSCode 开发工具](https://www.quanxiaoha.com/column/10019.html)\n  - [4.3 添加 vue-router 路由管理器](https://www.quanxiaoha.com/column/10020.html)\n  - [4.4 Vite 配置路径别名：更方便的引用文件](https://www.quanxiaoha.com/column/10021.html)\n  - [4.5 整合 Tailwind CSS](https://www.quanxiaoha.com/column/10022.html)\n  - [4.6 整合 Tailwind CSS 组件库：Flowbite](https://www.quanxiaoha.com/column/10023.html)\n  - [4.7 整合饿了么 Element Plus 组件库](https://www.quanxiaoha.com/column/10024.html)\n\n- 五、登录模块开发\n  - [5.1 登录页设计：支持响应式布局](https://www.quanxiaoha.com/column/10025.html)\n  - [5.2 登录页加点盐：通过 Animate.css 添加动画](https://www.quanxiaoha.com/column/10026.html)\n  - [5.3 整合 Mybatis Plus](https://www.quanxiaoha.com/column/10027.html)\n  - [5.4 p6spy 组件打印完整的 SQL 语句、执行耗时](https://www.quanxiaoha.com/column/10028.html)\n  - [5.5 整合 Spring Security](https://www.quanxiaoha.com/column/10029.html)\n  - [5.6 Spring Security 整合 JWT ：实现身份认证](https://www.quanxiaoha.com/column/10030.html)\n  - [5.7 Spring Security 整合 JWT ：实现接口鉴权](https://www.quanxiaoha.com/column/10031.html)\n  - [5.8 Vue 整合 Axios 实现登录功能](https://www.quanxiaoha.com/column/10032.html)\n  - [5.9 登录页表单验证](https://www.quanxiaoha.com/column/10033.html)\n  - [5.10 登录消息提示、回车键监听、按钮加载 Loading](https://www.quanxiaoha.com/column/10034.html)\n  - [5.11 存储 Token 到 Cookie 中](https://www.quanxiaoha.com/column/10035.html)\n  - [5.12 Axios 添加请求拦截器、响应拦截器](https://www.quanxiaoha.com/column/10036.html)\n  - [5.13 全局路由拦截：实现页面标题动态设置、后台路由跳转的登录判断](https://www.quanxiaoha.com/column/10037.html)\n  - [5.14 实现页面顶部加载 Loading 效果](https://www.quanxiaoha.com/column/10038.html)\n  - [5.15 重复登录问题优化、密码框可显示密码](https://www.quanxiaoha.com/column/10040.html)\n  - [5.16 角色鉴权：添加演示账号，仅支持查询操作](https://www.quanxiaoha.com/column/10089.html)\n  \n  \n  \n\n- 六、Element Plus 手搭 Admin 管理后台骨架\n  - [6.1 搭建管理后台基本布局](https://www.quanxiaoha.com/column/10039.html)\n  - [6.2 后台公共 Header 头：样式布局](https://www.quanxiaoha.com/column/10041.html)\n  - [6.3 后台公共左侧 Menu 菜单栏：样式布局](https://www.quanxiaoha.com/column/10042.html)\n  - [6.4 整合全局状态管理库 Pinia](https://www.quanxiaoha.com/column/10043.html)\n  - [6.5 左边菜单栏点击收缩、展开功能实现](https://www.quanxiaoha.com/column/10044.html)\n  - [6.6 支持全屏展示、页面点击刷新](https://www.quanxiaoha.com/column/10045.html)\n  - [6.7 标签导航栏组件实现：样式布局](https://www.quanxiaoha.com/column/10046.html)\n  - [6.8 标签导航栏组件实现：路由同步 (1)](https://www.quanxiaoha.com/column/10047.html)\n  - [6.9 标签导航栏组件实现：路由同步 (2)](https://www.quanxiaoha.com/column/10048.html)\n  - [6.10 标签导航栏组件实现：标签页关闭](https://www.quanxiaoha.com/column/10049.html)\n  - [6.11 标签导航栏组件实现：关闭其他、全部标签页](https://www.quanxiaoha.com/column/10050.html)\n  - [6.12 后台公共 Footer 页脚：样式布局](https://www.quanxiaoha.com/column/10051.html)\n  - [6.13 使用 KeepAlive 缓存组件，提高页面切换性能和响应速度](https://www.quanxiaoha.com/column/10052.html)\n  - [6.14 使用 Transition 组件添加全局过渡动画](https://www.quanxiaoha.com/column/10053.html)\n  - [6.15 修改用户密码接口开发](https://www.quanxiaoha.com/column/10054.html)\n  - [6.16 获取当前登录用户信息接口开发](https://www.quanxiaoha.com/column/10055.html)\n  - [6.17 Pinia 存储用户信息，动态显示登录用户名](https://www.quanxiaoha.com/column/10056.html)\n  - [6.18 使用 pinia-persist 插件实现 Pinia 数据持久化](https://www.quanxiaoha.com/column/10057.html)\n  - [6.19 用户修改密码、退出登录功能开发](https://www.quanxiaoha.com/column/10058.html)\n  - [6.20 小结](https://www.quanxiaoha.com/column/10059.html)\n\n  \n\n  \n- 七、管理后台：文章分类模块开发\n  - [7.1 分类模块接口分析](https://www.quanxiaoha.com/column/10060.html)\n  - [7.2 文章分类：新增接口开发](https://www.quanxiaoha.com/column/10061.html)\n  - [7.3 文章分类：分页接口开发](https://www.quanxiaoha.com/column/10062.html)\n  - [7.4 文章分类：删除接口开发](https://www.quanxiaoha.com/column/10063.html)\n  - [7.5 文章发布：分类 Select 下拉列表接口开发](https://www.quanxiaoha.com/column/10064.html)\n  - [7.6 后台分类管理页面：样式布局](https://www.quanxiaoha.com/column/10065.html)\n  - [7.7 Config Provider 全局配置: 实现组件中文化](https://www.quanxiaoha.com/column/10066.html)\n  - [7.8 文章分类：分页列表数据动态渲染](https://www.quanxiaoha.com/column/10067.html)\n  - [7.9 文章分类：新增功能开发](https://www.quanxiaoha.com/column/10068.html)\n  - [7.10 文章分类：删除功能开发](https://www.quanxiaoha.com/column/10069.html)\n  - [7.11 通用表单对话框组件封装](https://www.quanxiaoha.com/column/10070.html)\n  - [7.12 添加 Table 组件加载 Loading 、表单对话框提交按钮 Loading 动画](https://www.quanxiaoha.com/column/10071.html)\n  \n\n\n- 八、管理后台：标签模块开发\n  - [8.1 标签模块接口分析【视频讲解】](https://www.quanxiaoha.com/column/10072.html)\n  - [8.2 标签管理：新增标签接口开发【视频讲解】](https://www.quanxiaoha.com/column/10073.html)\n  - [8.3 标签管理：标签分页接口开发【视频讲解】](https://www.quanxiaoha.com/column/10074.html)\n  - [8.4 标签管理：删除标签接口开发【视频讲解】](https://www.quanxiaoha.com/column/10075.html)\n  - [8.5 标签关键词模糊查询 select 列表接口开发【视频讲解】](https://www.quanxiaoha.com/column/10076.html)\n  - [8.6 标签管理页面开发：分页列表【视频讲解】](https://www.quanxiaoha.com/column/10077.html)\n  - [8.7 标签管理页面开发：新增&删除标签功能【视频讲解】](https://www.quanxiaoha.com/column/10078.html)\n  \n  \n  \n- 九、管理后台：博客设置模块开发\n  - [9.1 博客设置模块功能分析、表设计](https://www.quanxiaoha.com/column/10079.html)\n  - [9.2 Docker 本地安装 Minio 对象存储](https://www.quanxiaoha.com/column/10080.html)\n  - [9.3 文件上传接口开发](https://www.quanxiaoha.com/column/10081.html)\n  - [9.4 博客设置: 更新接口开发](https://www.quanxiaoha.com/column/10082.html)\n  - [9.5 整合 Mapstruct : 简化属性映射](https://www.quanxiaoha.com/column/10083.html)\n  - [9.6 博客设置：获取详情接口开发](https://www.quanxiaoha.com/column/10084.html)\n  - [9.7 博客设置页面：样式布局](https://www.quanxiaoha.com/column/10085.html)\n  - [9.8 管理后台：滚动样式优化](https://www.quanxiaoha.com/column/10086.html)\n  - [9.9 博客设置页：数据渲染、图片上传](https://www.quanxiaoha.com/column/10087.html)\n  - [9.10 博客设置页：更新设置](https://www.quanxiaoha.com/column/10088.html)\n  \n\n\n\n- 十、管理后台：文章模块开发\n  - [10.1 文章管理模块功能分析、表设计](https://www.quanxiaoha.com/column/10090.html)\n  - [10.2 文章管理：文章发布接口开发（1）](https://www.quanxiaoha.com/column/10091.html)\n  - [10.3 文章管理：文章发布接口开发（2）—— SQL 注入器实现批量插入](https://www.quanxiaoha.com/column/10092.html)\n  - [10.4 文章管理：文章删除接口开发](https://www.quanxiaoha.com/column/10093.html)\n  - [10.5 文章管理：分页接口开发](https://www.quanxiaoha.com/column/10094.html)\n  - [10.6 文章管理：获取文章详情接口开发](https://www.quanxiaoha.com/column/10095.html)\n  - [10.7 文章管理：文章更新接口开发](https://www.quanxiaoha.com/column/10096.html)\n  - [10.8 文章管理：分页列表开发](https://www.quanxiaoha.com/column/10097.html)\n  - [10.9 文章管理页：删除文章开发](https://www.quanxiaoha.com/column/10098.html)\n  - [10.10 文章管理页：写文章对话框样式布局](https://www.quanxiaoha.com/column/10099.html)\n  - [10.11 文章管理页：文章发布功能开发](https://www.quanxiaoha.com/column/10100.html)\n  - [10.12 文章管理：获取所有标签 Select 列表接口开发](https://www.quanxiaoha.com/column/10101.html)\n  - [10.13 文章管理页：文章编辑功能开发](https://www.quanxiaoha.com/column/10102.html)\n  - [10.14 Bug 修复：分类、标签删除接口添加是否关联文章校验; 前端 token 过期问题 fixed](https://www.quanxiaoha.com/column/10103.html)\n  \n  \n  \n  \n  \n\n- 十一、博客前台：首页开发\n   - [11.1 前台首页、归档页接口分析](https://www.quanxiaoha.com/column/10104.html)\n   - [11.2 前台首页：文章分页接口开发](https://www.quanxiaoha.com/column/10105.html)\n   - [11.3 公共侧边栏：获取分类、标签列表接口开发](https://www.quanxiaoha.com/column/10106.html)\n   - [11.4 公共部分：获取博客设置信息接口开发](https://www.quanxiaoha.com/column/10107.html)\n   - [11.5 前台 Header 头组件封装](https://www.quanxiaoha.com/column/10108.html)   \n   - [11.6 首页样式布局设计（1）](https://www.quanxiaoha.com/column/10109.html)\n   - [11.7 首页样式布局设计（2） —— 侧边栏博主信息卡片](https://www.quanxiaoha.com/column/10110.html)\n   - [11.8 首页样式布局设计（3） —— 侧边栏分类、标签卡片](https://www.quanxiaoha.com/column/10111.html)\n   - [11.9 首页样式布局设计（4） —— Footer 组件封装](https://www.quanxiaoha.com/column/10112.html)\n   - [11.10 首页文章分页数据渲染](https://www.quanxiaoha.com/column/10113.html)\n   - [11.11 公共右边栏：博主信息卡片组件封装](https://www.quanxiaoha.com/column/10114.html)\n   - [11.12 公共右边栏：分类、标签卡片组件封装](https://www.quanxiaoha.com/column/10115.html)\n   - [11.13 公共 Header 头：跳转后台、退出登录功能开发](https://www.quanxiaoha.com/column/10116.html)\n   \n   \n\n- 十二、博客前台：归档列表页、分类列表页、标签列表页开发\n   - [12.1 归档页、分类列表页接口分析](https://www.quanxiaoha.com/column/10117.html)\n   - [12.2 文章归档分页接口开发](https://www.quanxiaoha.com/column/10118.html)\n   - [12.3 前台归档页：样式布局设计](https://www.quanxiaoha.com/column/10119.html)\n   - [12.4 前台归档页：分页列表功能开发](https://www.quanxiaoha.com/column/10120.html)\n   - [12.5 前台分类页开发](https://www.quanxiaoha.com/column/10121.html)\n   - [12.6 获取某个分类下的文章列表——分页接口开发](https://www.quanxiaoha.com/column/10122.html)\n   - [12.7 前台分类-文章列表页: 样式布局开发](https://www.quanxiaoha.com/column/10123.html)\n   - [12.8 分类-文章列表页开发](https://www.quanxiaoha.com/column/10124.html)\n   - [12.9 前台标签列表页：样式布局&功能开发](https://www.quanxiaoha.com/column/10125.html)\n   - [12.10 获取某个标签下的文章列表——分页接口开发](https://www.quanxiaoha.com/column/10130.html)\n   - [12.11 标签-文章列表页开发](https://www.quanxiaoha.com/column/10131.html)\n\n\n- 十三、博客前台：文章详情页开发\n   - [13.1 文章详情页接口分析](https://www.quanxiaoha.com/column/10126.html)\n   - [13.2 后端封装 Markdown 装换工具类](https://www.quanxiaoha.com/column/10127.html)\n   - [13.3 获取文章详情接口开发](https://www.quanxiaoha.com/column/10128.html)\n   - [13.4 文章详情页：样式布局设计](https://www.quanxiaoha.com/column/10129.html)\n\n   - *努力爆肝中，每天更新两小节, 按目前的更新速度，1.0 版本差不多还剩1个半月更新完毕...*\n- 十四、管理后台：仪表盘模块开发\n- 十五、项目部署上线\n  - 云服务器选购\n  - 相关环境安装（JDK、Docker、Nginx、Mysql）\n  - Nginx 配合 Spring Boot 部署\n  - 部署前端项目以及通过 IP 访问\n  - 域名选购\n  - 网站备案\n  - 域名映射，项目正式上线\n\n\n\n## 👨🏻‍💻 适用人群\n\n- **在校学生**，有一定基础，想做毕业设计，或者为找工作准备，需要实战项目加分；\n\n  > 💡 TIP: 小白也没关系，小哈将会告诉你学习路线是啥，哪里有免费的高质量学习视频可以白嫖，学完这些技术栈后再来做实战项目，或者学一点基础边实战边学习都可以。\n\n- **已经参与工作，对前后端分离感兴趣**，想学习 Vue 3 前端，对独立上线自己网站感兴趣的童鞋；\n- **想独立接私活**，需要同时会后端、前端技术栈的童鞋；\n\n## ✊ 如何加入？\n\n小哈已经将本站的专栏模块接入了知识星球，想要查看专栏内容，需要订阅我星球后，*微信扫码授权登录后即可解锁所有内容*。因为目前也是刚开始运营，所以价格不会太高，星球官方定价最低必须是 50 元。小哈最终定价为 <font class=\"text-xl\" style=\'color: red\'><b>限时 35 元（附 15 元的优惠券，记得扫码领取下方优惠券加入哟）</b></font>，后续随着内容慢慢的更新迭代，会慢慢涨上去，所以早加入更具性价比哟~ \n\n<font class=\"text-xl\" style=\'color: red\'><b>星球支持 3 天无理由退费</b></font>，感兴趣的小伙伴*可先加入，看看内容质量如何，不合适直接退款就行，觉得确实内容很干货，就留下来学习，无套路!*\n\n<div class=\"flex items-center justify-center text-lg text-red-500 font-bold mb-2\">扫描下方二维码加入, 星球支持 3 天无理由退款，可以先进去看看合不合适👇👇</div>\n\n![\"领取优惠券加入，更划算\"](https://img.quanxiaoha.com/quanxiaoha/169355760680941 \"领取优惠券加入，更划算\")\n\n<div class=\"flex items-center justify-center text-lg text-red-500 font-bold\">扫描上方二维码加入, 星球支持 3 天无理由退款，可以先进去看看合不合适👆👆</div>\n\n\n\n\n## ❓ 关于答疑\n\n小伙伴们如果在跟着专栏学习，手敲项目的过程中遇到问题，碰到无法解决的问题，**可在小哈的知识星球内部提问**，我会统一来解答, 如果星球说不清楚的，就加私人微信，打包发项目，亲自给你看哪一步有问题，保证跟上项目进度，不落下任何一个小伙伴，大家一起冲冲冲~\n\n## 😃 加微信咨询\n\n对专栏感兴趣的小伙伴，也可以加小哈私人微信来咨询，扫描下方二维码即可，记得备注【*咨询*】哟：\n\n![扫描二维码，添加小哈私人微信](https://img.quanxiaoha.com/quanxiaoha/169536889316499 \"扫描二维码，添加小哈私人微信\")\n');

-- ----------------------------
-- Table structure for t_article_tag_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_article_tag_rel`;
CREATE TABLE `t_article_tag_rel`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `article_id` bigint(20) UNSIGNED NOT NULL COMMENT '文章id',
  `tag_id` bigint(20) UNSIGNED NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id`) USING BTREE,
  INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章对应标签关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article_tag_rel
-- ----------------------------
INSERT INTO `t_article_tag_rel` VALUES (19, 7, 12);
INSERT INTO `t_article_tag_rel` VALUES (20, 8, 28);
INSERT INTO `t_article_tag_rel` VALUES (24, 9, 28);
INSERT INTO `t_article_tag_rel` VALUES (26, 10, 29);
INSERT INTO `t_article_tag_rel` VALUES (27, 11, 27);
INSERT INTO `t_article_tag_rel` VALUES (31, 13, 27);
INSERT INTO `t_article_tag_rel` VALUES (32, 13, 30);
INSERT INTO `t_article_tag_rel` VALUES (33, 13, 31);
INSERT INTO `t_article_tag_rel` VALUES (34, 14, 36);
INSERT INTO `t_article_tag_rel` VALUES (39, 15, 27);
INSERT INTO `t_article_tag_rel` VALUES (40, 12, 35);
INSERT INTO `t_article_tag_rel` VALUES (47, 17, 30);
INSERT INTO `t_article_tag_rel` VALUES (49, 16, 42);

-- ----------------------------
-- Table structure for t_blog_settings
-- ----------------------------
DROP TABLE IF EXISTS `t_blog_settings`;
CREATE TABLE `t_blog_settings`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `logo` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '博客Logo',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '博客名称',
  `author` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '作者名',
  `introduction` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '介绍语',
  `avatar` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '作者头像',
  `github_homepage` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'GitHub 主页访问地址',
  `csdn_homepage` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'CSDN 主页访问地址',
  `gitee_homepage` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Gitee 主页访问地址',
  `zhihu_homepage` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '知乎主页访问地址',
  `mail` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '博主邮箱地址',
  `is_comment_sensi_word_open` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否开启评论敏感词过滤, 0:不开启；1：开启',
  `is_comment_examine_open` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否开启评论审核, 0: 未开启；1：开启',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客设置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_blog_settings
-- ----------------------------
INSERT INTO `t_blog_settings` VALUES (1, 'http://127.0.0.1:9000/weblog/c46379070c544e09a7868387077a2ed3.png', '个人博客', '粟英朝', '多吃饭，', 'http://127.0.0.1:9000/weblog/c46379070c544e09a7868387077a2ed3.png', '', 'https://blog.csdn.net/zai_zhong_?spm=1000.2115.3001.5343', '', '', '', 1, 0);

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位：0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (3, 'sssss', '2025-04-20 17:37:18', '2025-04-20 17:37:18', 0);
INSERT INTO `t_category` VALUES (8, 'C', '2025-04-20 21:39:05', '2025-04-20 21:39:05', 0);
INSERT INTO `t_category` VALUES (33, 'ssshh', '2025-04-20 17:37:23', '2025-04-20 17:37:23', 0);
INSERT INTO `t_category` VALUES (45, 'spring', '2025-04-20 17:37:46', '2025-04-20 17:37:46', 0);
INSERT INTO `t_category` VALUES (48, 'Test', '2025-05-13 23:01:57', '2025-05-13 23:01:57', 0);
INSERT INTO `t_category` VALUES (50, 'Tools', '2025-06-16 23:06:52', '2025-06-16 23:06:52', 0);
INSERT INTO `t_category` VALUES (53, '学习笔记', '2025-12-13 18:16:49', '2025-12-13 18:16:49', 0);

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论内容',
  `avatar` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `nickname` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `mail` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `website` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站地址',
  `router_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论所属的路由',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标志位：0：未删除 1：已删除',
  `reply_comment_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '回复的评论 ID',
  `parent_comment_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父评论 ID',
  `reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '原因描述',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '1: 待审核；2：正常；3：审核未通过;',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_router_url`(`router_url`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_reply_comment_id`(`reply_comment_id`) USING BTREE,
  INDEX `idx_parent_comment_id`(`parent_comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_comment
-- ----------------------------

-- ----------------------------
-- Table structure for t_statistics_article_pv
-- ----------------------------
DROP TABLE IF EXISTS `t_statistics_article_pv`;
CREATE TABLE `t_statistics_article_pv`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pv_date` date NOT NULL COMMENT '被统计的日期',
  `pv_count` bigint(20) UNSIGNED NOT NULL COMMENT 'pv访问量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_pv_date`(`pv_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '统计表 - 文章 PV (访问量)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_statistics_article_pv
-- ----------------------------
INSERT INTO `t_statistics_article_pv` VALUES (1, '2025-12-14', 0, '2025-12-13 23:00:00', '2025-12-13 23:00:00');

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位：0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES (12, 'java', '2025-05-09 01:02:46', '2025-05-09 01:02:46', 0);
INSERT INTO `t_tag` VALUES (27, 'test', '2025-05-14 22:57:20', '2025-05-14 22:57:20', 0);
INSERT INTO `t_tag` VALUES (28, '哈哈', '2025-05-15 23:17:06', '2025-05-15 23:17:06', 0);
INSERT INTO `t_tag` VALUES (29, 'git', '2025-06-16 23:06:32', '2025-06-16 23:06:32', 0);
INSERT INTO `t_tag` VALUES (30, 'test2', '2025-06-16 23:09:09', '2025-06-16 23:09:09', 0);
INSERT INTO `t_tag` VALUES (31, 'test3', '2025-06-16 23:09:16', '2025-06-16 23:09:16', 0);
INSERT INTO `t_tag` VALUES (34, 'test6', '2025-06-16 23:09:28', '2025-06-16 23:09:28', 0);
INSERT INTO `t_tag` VALUES (35, 'Tools', '2025-06-16 23:13:55', '2025-06-16 23:13:55', 0);
INSERT INTO `t_tag` VALUES (36, 'z', '2025-06-17 09:15:54', '2025-06-17 09:15:54', 0);
INSERT INTO `t_tag` VALUES (42, 'Linux', '2025-12-13 18:18:24', '2025-12-13 18:18:24', 0);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (8, 'zifengliu', '$2a$10$4s8T9rjoZE9U3UGAkKjo5.VMz3mVLA8LK85dnraRGnqZ0Fq/v4oNa', '2025-03-04 23:11:33', '2025-04-20 21:15:36', 0);
INSERT INTO `t_user` VALUES (9, 'admin', '$2a$10$VDthRVh2MJndBP2xa6cJFOMKO1YePzyC4973QVBX8wWzXoXtUOnXm', '2025-03-17 16:20:23', '2025-03-17 16:20:23', 0);
INSERT INTO `t_user` VALUES (11, 'test1', '$2a$10$m2smFkHGDytLKPtI6cXbAOEv3Ybbh1zTibs60FLdIq6WHYmnr2wrq', '2025-03-27 21:41:58', '2025-03-27 22:41:17', 0);
INSERT INTO `t_user` VALUES (14, 'test', '$2a$10$bYc9Q3ytEpZ8Tz1VUWcUnez7CNaJ4Ql1tgl2nTh8pCGbCddxO2NBm', '2025-12-12 18:00:25', '2025-12-12 18:00:25', 0);

-- ----------------------------
-- Table structure for t_wiki
-- ----------------------------
DROP TABLE IF EXISTS `t_wiki`;
CREATE TABLE `t_wiki`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `cover` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '封面',
  `summary` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '摘要',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标志位：0：未删除 1：已删除',
  `weight` int(6) UNSIGNED NOT NULL DEFAULT 0 COMMENT '权重，用于是否置顶（0: 未置顶；>0: 参与置顶，权重值越高越靠前）',
  `is_publish` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否发布：0：未发布 1：已发布',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识库表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_wiki
-- ----------------------------

-- ----------------------------
-- Table structure for t_wiki_catalog
-- ----------------------------
DROP TABLE IF EXISTS `t_wiki_catalog`;
CREATE TABLE `t_wiki_catalog`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `wiki_id` bigint(20) UNSIGNED NOT NULL COMMENT '知识库id',
  `article_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '文章id',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `level` tinyint(2) NOT NULL DEFAULT 1 COMMENT '目录层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父目录id',
  `sort` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  `is_deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标志位：0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_id`(`article_id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE,
  INDEX `idx_wiki_id`(`wiki_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识库目录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_wiki_catalog
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

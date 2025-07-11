-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question(
   id bigint auto_increment comment 'id' primary key ,
   title varchar(512) null comment '题目名称',
   tags varchar(1024) null comment '题目标签',
   content text null comment '题目描述',
   judgeCase text null comment '题目测试用例',
   answer text null comment '题目答案',
   submitNum int default 0 not null comment '提交次数',
   acceptedNum  int  default 0 not null comment '题目通过数',
   thumbNum   int    default 0 not null comment '点赞数',
   favourNum  int      default 0 not null comment '收藏数',
   userId     bigint not null comment '创建用户 id',
   createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
   isDelete   tinyint  default 0                 not null comment '是否删除',
   index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(32)                        not null comment '语言',
    code       text                               not null comment '用户代码',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    judgeInfo  varchar(512)                       not null comment '判题信息',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
)comment '题目提交';

-- 评论表
create table if not exists comment(
    id          bigint auto_increment comment 'id' primary key ,
    userId      bigint                      not null comment '用户id，用于关联用户表',
    questionId  bigint                      not null comment '题目id，用于关联题目表',
    content     varchar(512)                not null comment '评论内容',
    beCommentId bigint                               comment '被评论id，用于二级评论',
    thumbNum   int                default 0 not null comment '点赞数',
    replyNum  int                 default 0 not null comment '回复数',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId(questionId),
    index idx_userId(userId)
)comment '评论表';
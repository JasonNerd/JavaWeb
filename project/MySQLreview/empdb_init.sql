create schema if not exists db_emp;
use db_emp;
create table if not exists tb_emp
(
    id          int,
    username    varchar(20)                not null comment '用户名，非空且唯一',
    name        varchar(10)                null comment '姓名',
    gender      tinyint unsigned default 1 null comment '性别：1男2女',
    image       varchar(200)               null comment '用户照片资源路径字符串',
    job         tinyint unsigned           null comment '职位 1班主任 2讲师 3学工主管 4教研主管',
    entrydate   date                       null comment '入职日期',
    create_time datetime                   not null comment '条目创建时间，非空',
    update_time datetime                   null comment '条目最后更新时间，非空'
)comment '员工信息表';

create unique index tb_emp_id_uindex on tb_emp (id);

alter table tb_emp add constraint tb_emp_pk primary key (id);

alter table tb_emp modify id int auto_increment;

create unique index tb_emp_username_uindex on tb_emp (username);

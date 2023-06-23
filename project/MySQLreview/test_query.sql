
-- update test
update tb_emp set entrydate='2023-05-28', update_time=now() where name = '马超';
update tb_emp set job=null, update_time=now() where username='wzjun';

-- query test
-- 1. condition query
-- -- 1-1. 查询姓名为 刘备 的员工信息
select * from tb_emp where name = '刘备';

-- -- 1-2. 查询 没有分配职位 的员工信息
select * from tb_emp where job is null;

-- -- 1-3. 否询 入职日期 在2000-01-01(包含) 到2010-01-01(包含)之的员工信息
select * from tb_emp where entrydate between '2000-01-01' and '2010-01-01';

-- -- 1-4. 查询 姓名 为两个字的员工信息
select * from tb_emp where name like '__';

-- -- 1-5. 查询 职位是 2 (讲师)，3 (学工主管)，4 (教研主管) 的员工信息
select * from tb_emp where job=2 or job=3 or job=4;

-- -- 1-5. 查询姓 张 的员工信息
select * from tb_emp where name like '张%';

-- 2. statistic query
-- -- 2-1. 统计 tb_emp 记录条数
select count(*) from tb_emp;    -- 28

-- -- 2-2. 统计 tb_emp 性别分布, 注意重命名
select if(gender=1, '先生', '女士') 性别, count(*) 人数 from tb_emp group by gender;

-- -- 2-3. 统计 tb_emp 职位分布, 注意可能包含空值
select (case job
            when 1 then '班主任'
            when 2 then '讲师'
            when 3 then '学工主管'
            when 4 then '教研主管'
            else '未录入' end
           ) 职位, count(*) 人数
from tb_emp group by job order by 人数 desc;

-- -- 2-4. 统计 最早员工入职时间
select min(entrydate) from tb_emp;    -- '2003-09-17'

-- -- 2-5. 统计 最迟入职员工
select max(entrydate) from tb_emp;    -- '2023-05-28'

-- -- 2-6. 统计 字段平均值(ID)
select avg(id) from tb_emp;

-- -- 2-7. 统计 字段和(ID)
select sum(id) from tb_emp;

-- 3. 其他查询
-- -- 3-1. 根据入职时间，对员工进行降序排序
select * from tb_emp order by entrydate desc;

-- -- 3-2. 根据 入职时间 对公司的员工进行升序排序, 入职时间相同时再按照 更新时间 进行降序排序
select * from tb_emp order by entrydate ,update_time desc;

-- -- 3.3 假设每页展示7条数据, 现在要查询第3页的数据
select * from tb_emp limit 14, 7;

-- -- 3.4 查询入职时间在 2015-01-01 (包含)以前的员工, 并对结果根职位分组, 获取员工数量大于等于2的职位
select job, count(*) from tb_emp where entrydate<='2015-01-01' group by job having count(*)>=2;

-- -- 3.5 根据输入的 员工姓名、员工性别、入职时间 搜索满足条件的员工信息。
-- -- 其中 员工姓名 支持模糊匹配; 性别 进行精确查询，入职时间 进行范围查询
-- -- 支持分页查询
-- -- 并对查询的结果，根据最后修改时间进行倒序排序.
-- -- 具体的, 查询 员工姓名包含'王' 性别为女 入职时间在'2000-01-01'和'2015-12-31'之间
-- -- 的第一页数据, 每页10条
select * from tb_emp where
    name like '%王%' and
    gender = 2 and
    entrydate between '2000-01-01' and '2015-12-31'
order by update_time desc
limit 0, 10;


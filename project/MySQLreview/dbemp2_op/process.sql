-- 多表查询案例
-- 1. 查询员工的姓名及所属的部门名称(隐式内连接实现)
select tb_emp.name, tb_dept.name
from tb_emp,
     tb_dept
where tb_emp.dept_id = tb_dept.id;
-- 一共 16 条数据, 我们发现 陈友谅 没有所属部门, 因此未查询到

-- 2. 在1的基础上使用外连接, 要求所有员工都查询到
select te.name, td.name
from tb_emp te
         left join tb_dept td on te.dept_id = td.id;

-- 3. 在1的基础上使用外连接, 要求所有的部门都查询到
select te.name, td.name
from tb_dept td
         left join tb_emp te on te.dept_id = td.id;


-- 4. 查询 教研部 的所有员工信息
select *
from tb_emp
where tb_emp.dept_id =
      (select id from tb_dept where name = '教研部');

-- 5. 查询 教研部 和 咨询部 的所有员工信息 (列子查询)
select *
from tb_emp
where tb_emp.dept_id in
      (select id from tb_dept where name = '教研部' or name = '咨询部');

-- 7. 查询与 韦一笑 的入职日期 及 职位 都相同的员工信息 (行子查询)
select *
from tb_emp
where (entrydate, job) = (select entrydate, job from tb_emp where name = '韦一笑');

-- 8. 查询入职日期是 2006-01-01 之后的员工信息及其部门名称 (表子查询)
select tb_emp.*, tb_dept.name from tb_emp, tb_dept where tb_emp.dept_id=tb_dept.id and entrydate > '2006-01-01';
-- 使用子查询: 先把表范围缩小, 再进行连接
select e.*, d.name from (select * from tb_emp where entrydate > '2006-01-01') e, tb_dept d where e.dept_id=d.id;

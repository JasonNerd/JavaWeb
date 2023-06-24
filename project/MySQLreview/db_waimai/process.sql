-- 1. 查询价格低于 10元的菜品的名称、价格及其菜品的分类名称
select dl.name, dl.price, category.name
from (select name, price, category_id from dish where price < 10) dl,
     category
where dl.category_id = category.id;

-- 2. 查询所有价格在10元(含)到50元(含)之间 且状态为"起售"的菜品名称、价格及其分类名称
-- 即使菜品没有分类, 查询结果也需要展示出来
select dbt.name, dbt.price, category.name
from (select name, price, category_id from dish where price between 10 and 50 and status = 1) dbt
         left join category
                   on dbt.category_id = category.id;

-- 3. 查询每个分类下最贵的菜品,展示出分类的名称、最贵的菜品的价格
select category.name, cd.max_price
from (select category_id, max(price) max_price from dish group by category_id) cd,
     category
where cd.category_id = category.id;

-- 4. 查询 "起售”状态菜品数量大于等于3的 分类名称
select c.name
from dish d,
     category c
where d.category_id = c.id
  and d.status = 1
group by c.name
having count(*) >= 3;

-- 5. 查询出"商务套餐A”中包含了哪些菜品 (展示出套餐名称、价格,包含的菜品名称、价格、份数)
select smd.name, smd.price, dish.name, dish.price, smd.copies
from dish,
     (select setmeal.name, setmeal.price, setmeal_dish.copies, setmeal_dish.dish_id
      from setmeal,
           setmeal_dish
      where setmeal_dish.setmeal_id = setmeal.id
        and setmeal.name = '商务套餐A') smd
where smd.dish_id = dish.id;
-- 提示, 也可以直接三个表全连上

-- 6. 查询出低于菜品平均价格的菜品信息(展示出菜品名称、菜品价格)
select name, price
from dish
where price < (select avg(price) from dish);



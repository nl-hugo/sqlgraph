-- (1) union all
insert into table1 (col1, col2)
select colA, coalesce(colB, 'unknown') from table2 
union all 
select colC, 'd' from table3;

-- (2) left join
insert into table1
select * from table2 left join table3 on a=b;

-- (3) join subquery
insert into table1
select * from table2 join (select * from table3) on a=b;

-- (4) join nested subquery
insert into table1
select * from table2 join (select * from table3 left join table4 on 1=2) on a=b;

-- (5) join in where condition
--     where conditions do not count towards data lineage!
insert into table1
select * from table2 
where colA not in (select col1 from table3 full outer join table4 on 1=2);

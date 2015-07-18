-- (1) values, single row, with named columns
insert into table1 (col1, col2)
values (1, "a");

-- (2) values, multiple rows, no named columns
insert into table1 (col1, col2)
values (1, "a"), (2, "b");

-- (3) subquery, named columns, with function
insert into table1 (col1, col2)
select colA, coalesce(colB, 'unknown') from table2;

-- (4) subquery, no named columns, no alias
insert into vcs.table1
select * from table2;

-- (5) subquery, no named columns, with alias
insert into table1
select t0.* from table2 t0;
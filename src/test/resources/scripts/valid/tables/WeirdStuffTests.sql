-- (1) create table with different CaSiNg
create table table1 as
select *
from TABLE2 t0
left join tAblE2 t01
on t0.colA = t01.colA
;

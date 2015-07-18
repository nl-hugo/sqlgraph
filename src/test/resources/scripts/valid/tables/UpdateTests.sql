-- (1) simple update
update table1 set colA = "a";

-- (2) referenced table
update table1 set colA = b.colB
from table2 b where colB = 1;

-- (3) multiple referenced tables
update table1 set colA = b.colB + c.colC
from table2 b, table3 c where colB = 1 and b.colA = c.colA;

-- (4) referenced table and where condition
update table1 set colA = b.colB
from table2 b where b.colB = 1 and b.colC not in (select a from table3);

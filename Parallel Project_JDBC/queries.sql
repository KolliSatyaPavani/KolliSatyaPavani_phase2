create table Customer(
mobileNo varchar2(30) ,
name varchar2(30),
balance number(10,2),
primary key(mobileNo));

create table recentTransactions(
mobileNo varchar2(30),
transactionType varchar2(30),
amount number(10,2),
foreign key(mobileNo) references customer(mobileNo));



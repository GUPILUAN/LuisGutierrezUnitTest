--- Crear la tabla

create table usuarios (
   id         number(19,0),
   username   varchar2(30 byte),
   password   varchar2(10 byte),
   email      varchar2(100 byte),
   isloggedin number(1,0),
   constraint usuarios_pk primary key ( id )
);

create sequence usuarios_seq start with 1 increment by 1 nocache;

---

select *
  from usuarios;

delete from usuarios;




-- Para reiniciar la secuencia
declare
   max_id number;
begin
   select nvl(
      max(id),
      0
   )
     into max_id
     from usuarios;
   execute immediate 'DROP SEQUENCE usuarios_seq'; -- Eliminar la secuencia
   execute immediate 'CREATE SEQUENCE usuarios_seq START WITH '
                     || ( max_id + 1 )
                     || ' INCREMENT BY 1'; -- Crear la secuencia
end;

select usuarios_seq.nextval
  from dual;

select table_name
  from all_tables
 where owner = 'SYSTEM';
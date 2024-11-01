select * from usuarios;

delete from usuarios;

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

select usuarios_seq.nextval from dual;

select table_name
  from all_tables
 where owner = 'SYSTEM';
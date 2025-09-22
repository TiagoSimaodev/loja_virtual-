CREATE TABLE public.tabela_acesso_end_point(
  nome_and_point character varying,
  qtd_acesso_end_point integer);
  
  INSERT INTO public.tabela_acesso_end_point(
            nome_and_point, qtd_acesso_end_point)
    VALUES ('END-POINT-NOME-PESSOA-FISICA', 0);

alter table  tabela_acesso_end_point add constraint nome_and_point_unique UNIQUE (nome_and_point);   
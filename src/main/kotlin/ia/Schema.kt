package ia

object Schema {

    val schema =   """
    create table if not exists public.hospital
(
    codigo varchar(50)  not null
        primary key,
    nombre varchar(100) not null
)

create table if not exists public.departamento
(
    codigo          varchar(50)  not null,
    nombre          varchar(100) not null,
    hospital_codigo varchar(50)  not null
        references public.hospital,
    primary key (codigo, hospital_codigo)
)
   

create table if not exists public.unidad
(
    codigo              varchar(50)  not null,
    nombre              varchar(100) not null,
    ubicacion           varchar(200) not null,
    departamento_codigo varchar(50)  not null,
    hospital_codigo     varchar(50)  not null,
    primary key (codigo, departamento_codigo, hospital_codigo),
    foreign key (departamento_codigo, hospital_codigo) references public.departamento
        on delete cascade
)
  

create table if not exists public.medico
(
    codigo              varchar(50)  not null
        primary key,
    nombre              varchar(100) not null,
    apellidos           varchar(100) not null,
    especialidad        varchar(100) not null,
    numero_licencia     varchar(50)  not null
        unique,
    telefono            varchar(20),
    anios_experiencia   integer,
    datos_contacto      text,
    unidad_codigo       varchar(50)  not null,
    departamento_codigo varchar(50)  not null,
    hospital_codigo     varchar(50)  not null,
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
        on update cascade on delete set null
)
   



create table if not exists public.paciente
(
    ci               varchar(50)  not null
        primary key,
    nombre           varchar(100) not null,
    apellidos        varchar(100) not null,
    fecha_nacimiento date         not null
        constraint paciente_fecha_nacimiento_check
            check (fecha_nacimiento <= (CURRENT_DATE - '16 years'::interval)),
    direccion        text         not null
)
    

create table if not exists public.causanoatencion
(
    causa_id    serial
        primary key,
    descripcion varchar(200) not null
        unique
)
    

create table if not exists public.informe
(
    numero_informe      integer                             not null,
    unidad_codigo       varchar(50)                         not null,
    departamento_codigo varchar(50)                         not null,
    hospital_codigo     varchar(50)                         not null,
    fecha_hora          timestamp default CURRENT_TIMESTAMP not null,
    pacientes_atendidos integer   default 0                 not null,
    pacientes_alta      integer   default 0                 not null,
    pacientes_admitidos integer   default 0                 not null,
    total_registro      integer   default 0                 not null,
    primary key (numero_informe, unidad_codigo, departamento_codigo, hospital_codigo),
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
)

create table if not exists public.turno
(
    numero_turno        integer     not null,
    unidad_codigo       varchar(50) not null,
    departamento_codigo varchar(50) not null,
    hospital_codigo     varchar(50) not null,
    medico_codigo       varchar(50) not null
        references public.medico
            on delete cascade,
    fecha               date        not null,
    primary key (numero_turno, unidad_codigo, departamento_codigo, hospital_codigo, medico_codigo),
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
        on update cascade on delete cascade
)
    

create table if not exists public.historiaclinica
(
    numero          varchar not null,
    ci_paciente     varchar not null
        unique
        references public.paciente,
    unidad_codigo   varchar not null,
    depart_codigo   varchar not null,
    hospital_codigo varchar not null,
    primary key (numero, ci_paciente, unidad_codigo, depart_codigo, hospital_codigo),
    constraint historiaclinica_unidad_codigo_depart_codigo_hospital_codig_fkey
        foreign key (unidad_codigo, depart_codigo, hospital_codigo) references public.unidad
            on update cascade on delete set null
)
    
create table if not exists public.registro
(
    registro_id         bigint generated by default as identity,
    unidad_codigo       varchar(50)                                        not null,
    departamento_codigo varchar(50)                                        not null,
    hospital_codigo     varchar(50)                                        not null,
    paciente_id         varchar(50)                                        not null
        references public.historiaclinica (ci_paciente)
            on update cascade on delete set null,
    fecha_registro      timestamp with time zone default CURRENT_TIMESTAMP not null,
    estado              varchar(20)                                        not null
        constraint registro_estado_check
            check ((estado)::text = ANY
                   (ARRAY [('pendiente'::character varying)::text, ('atendido'::character varying)::text, ('alta'::character varying)::text, ('fallecido'::character varying)::text, ('no atendido'::character varying)::text])),
    primary key (registro_id, unidad_codigo, departamento_codigo, hospital_codigo),
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
        on update cascade on delete set null
)


create table if not exists public.consulta
(
    consulta_id               serial,
    turno_numero              integer                             not null,
    turno_unidad_codigo       varchar(50)                         not null,
    turno_departamento_codigo varchar(50)                         not null,
    turno_hospital_codigo     varchar(50)                         not null,
    fecha_hora                timestamp default CURRENT_TIMESTAMP not null,
    id_registro               bigint                              not null,
    id_medico                 varchar                             not null,
    unidad_reg                varchar,
    departamento_reg          varchar,
    hospital_reg              varchar,
    primary key (consulta_id, turno_numero, turno_unidad_codigo, turno_departamento_codigo, turno_hospital_codigo,
                 id_medico),
    constraint consulta_turno_numero_turno_unidad_codigo_turno_departamen_fkey
        foreign key (turno_numero, turno_unidad_codigo, turno_departamento_codigo, turno_hospital_codigo,
                     id_medico) references public.turno
            on update cascade on delete set null,
    constraint consulta_unidad_reg_id_registro_departamento_reg_hospital__fkey
        foreign key (unidad_reg, id_registro, departamento_reg, hospital_reg) references public.registro ()
            on update cascade on delete set null
)
      """
}
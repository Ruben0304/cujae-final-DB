package ia

object Schema {

    val schema =   """
    create table if not exists public.hospital
(
    codigo varchar(50)  not null
        primary key,
    nombre varchar(100) not null
);
create table if not exists public.departamento
(
    codigo          varchar(50)  not null,
    nombre          varchar(100) not null,
    hospital_codigo varchar(50)  not null
        references public.hospital,
    primary key (codigo, hospital_codigo)
);
create table if not exists public.unidad
(
    codigo              varchar(50)  not null,
    nombre              varchar(100) not null,
    ubicacion           varchar(200) not null,
    departamento_codigo varchar(50)  not null,
    hospital_codigo     varchar(50)  not null,
    primary key (codigo, departamento_codigo, hospital_codigo),
    foreign key (departamento_codigo, hospital_codigo) references public.departamento
);
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
);
create table if not exists public.paciente
(
    numero_historia_clinica varchar(50)  not null,
    nombre                  varchar(100) not null,
    apellidos               varchar(100) not null,
    fecha_nacimiento        date         not null
        constraint paciente_fecha_nacimiento_check
            check (fecha_nacimiento <= (CURRENT_DATE - '16 years'::interval)),
    direccion               text         not null,
    unidad_codigo           varchar(50)  not null,
    departamento_codigo     varchar(50)  not null,
    hospital_codigo         varchar(50)  not null,
    primary key (numero_historia_clinica, unidad_codigo, departamento_codigo, hospital_codigo),
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
);
create table if not exists public.registro
(
    registro_id                      serial
        primary key,
    unidad_codigo                    varchar(50)               not null,
    departamento_codigo              varchar(50)               not null,
    hospital_codigo                  varchar(50)               not null,
    paciente_numero_historia_clinica varchar(50)               not null,
    fecha_registro                   date default CURRENT_DATE not null,
    estado                           varchar(20)               not null
        constraint registro_estado_check
            check ((estado)::text = ANY
                   (ARRAY [('Pendiente'::character varying)::text, ('Atendido'::character varying)::text, ('Alta'::character varying)::text, ('Fallecido'::character varying)::text, ('No Atendido'::character varying)::text])),
    constraint registro_unidad_codigo_departamento_codigo_hospital_codigo_fkey
        foreign key (unidad_codigo, departamento_codigo, hospital_codigo,
                     paciente_numero_historia_clinica) references public.paciente
);
create table if not exists public.causanoatencion
(
    causa_id    serial
        primary key,
    descripcion varchar(200) not null
        unique
);
create table if not exists public.registronoatendido
(
    registro_id integer not null
        primary key
        references public.registro,
    causa_id    integer not null
        references public.causanoatencion
);
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
);
create table if not exists public.turno
(
    numero_turno        integer           not null,
    unidad_codigo       varchar(50)       not null,
    departamento_codigo varchar(50)       not null,
    hospital_codigo     varchar(50)       not null,
    medico_codigo       varchar(50)       not null
        references public.medico,
    fecha               date              not null,
    pacientes_atendidos integer default 0 not null,
    pacientes_asignados integer default 0 not null,
    primary key (numero_turno, unidad_codigo, departamento_codigo, hospital_codigo),
    foreign key (unidad_codigo, departamento_codigo, hospital_codigo) references public.unidad
);


create table if not exists public.consulta
(
    consulta_id                      serial
        primary key,
    turno_numero                     integer                             not null,
    turno_unidad_codigo              varchar(50)                         not null,
    turno_departamento_codigo        varchar(50)                         not null,
    turno_hospital_codigo            varchar(50)                         not null,
    paciente_numero_historia_clinica varchar(50)                         not null,
    paciente_unidad_codigo           varchar(50)                         not null,
    paciente_departamento_codigo     varchar(50)                         not null,
    paciente_hospital_codigo         varchar(50)                         not null,
    fecha_hora                       timestamp default CURRENT_TIMESTAMP not null,
    constraint consulta_turno_numero_turno_unidad_codigo_turno_departamen_fkey
        foreign key (turno_numero, turno_unidad_codigo, turno_departamento_codigo,
                     turno_hospital_codigo) references public.turno,
    constraint consulta_paciente_numero_historia_clinica_paciente_unidad__fkey
        foreign key (paciente_numero_historia_clinica, paciente_unidad_codigo, paciente_departamento_codigo,
                     paciente_hospital_codigo) references public.paciente
);
    """
}
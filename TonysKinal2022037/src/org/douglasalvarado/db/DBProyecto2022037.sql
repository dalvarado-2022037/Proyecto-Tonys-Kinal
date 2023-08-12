/*
	Douglas Humberto Alvarado Morales
    2022037
    IN5AV
    Fecha de Creación:
		28-03-2023
    Fecha de Modificaciones:
		28-03-2023
		01-04-2023
        08-04-2023
        24-05-2023
*/
Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionPlato varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    catidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

Create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar (150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar (50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key 
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidadPlato int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato(codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null auto_increment,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Platos foreign key(codigoPlato)
		references Platos(codigoPlato),
	constraint FK_Productos_has_Platos_Producto foreign key(codigoProducto)
		references Productos (codigoProducto)
);

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null auto_increment,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio ),
    constraint FK_Servicios_has_Platos_Servicios foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null auto_increment,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)
);

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

-- ---------------------------------- PROCEDIMIENTOS ALMACENADOS ---------------------------

-- ---------------------------------- EMPRESAS ---------------------------------------------
-- Agregar Empresa
Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(8))
		Begin
			Insert into Empresas(nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono);
		End$$
Delimiter ;
call sp_AgregarEmpresa('Mc Donals','18-28 zona 9 Plaza Barrios','85489721');
call sp_AgregarEmpresa('Gerencia val Verde','18-12 zona 3 Zona Central','38561236');
call sp_AgregarEmpresa('Librerias San Angel','9-12 zona 5 MetaTerminal','23920237');
call sp_AgregarEmpresa('Directorio Terminal','20-29 zona 8 Portales','27301987');
call sp_AgregarEmpresa('Guildeuns','2-12 zona 8 colonia San Pedro','34228023');
call sp_AgregarEmpresa('Pizza Hut','9-8 zona 4 colonia San Cebastian','23889128');
call sp_AgregarEmpresa('Gomez escobar','34-1 zona 9 colonia Santo Domingo','48120385');
call sp_AgregarEmpresa('Tuertocion','9-21 zona 2 Faciolomal','38129502');
call sp_AgregarEmpresa('Vienestar Los olivas','2-39 zona 6 Santa Fe','23789231');
call sp_AgregarEmpresa('Santa fe','12-59 zona 8 MetaTerminal','92371239');

-- Editar Empresa
Delimiter $$
	Create procedure sp_EditarEmpresa(in codigoEmpresaIngreso int, in nombreEmpresaIngreso varchar(150), in direccionIngreso varchar (150), in telefonoIngreso varchar(8))
		Begin
			Update Empresas as E
				set E.nombreEmpresa = nombreEmpresaIngreso,
					E.direccion = direccionIngreso,
					E.telefono = telefonoIngreso
						where E.codigoEmpresa = codigoEmpresaIngreso;
        End$$
Delimiter ;
call sp_EditarEmpresa(1,'Mc Donal','18-28 zona 9 Plaza Antonio','46579812');

-- Eliminar Empresa
Delimiter $$
	Create procedure sp_EliminarEmpresa(in codigoEmpresaIngreso int)
		Begin
			Delete from Empresas
				where codigoEmpresa = codigoEmpresaIngreso;
        End$$
Delimiter ;
-- call sp_EliminarEmpresa(1);

-- Listar Empresas
Delimiter $$
	Create procedure sp_ListarEmpresas()
		Begin
			Select 
				codigoEmpresa,
				nombreEmpresa,
                direccion, 
                telefono
				from Empresas;
        End$$
Delimiter ;
call sp_ListarEmpresas();

-- Buscar Empresa
Delimiter $$
	Create procedure sp_BuscarEmpresa(in codigoEmpresaIngreso int)
		Begin
			Select 
				E.codigoEmpresa, 
                E.nombreEmpresa, 
                E.direccion, 
                E.telefono
                from Empresas E 
					where E.codigoEmpresa = codigoEmpresaIngreso;
        End$$
Delimiter ;
call sp_BuscarEmpresa(1);

-- ---------------------------------- TIPO EMPLEADO ----------------------------------------
-- Agregar Tipo Empleado
Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin
			Insert into TipoEmpleado (descripcion)
				values (descripcion);
        End$$
Delimiter ;
call sp_AgregarTipoEmpleado('Limpiza');
call sp_AgregarTipoEmpleado('Limpieza');
call sp_AgregarTipoEmpleado('Cocina');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Decoracion');
call sp_AgregarTipoEmpleado('Seguridad');
call sp_AgregarTipoEmpleado('Guardia');
call sp_AgregarTipoEmpleado('Atencion al cliente');
call sp_AgregarTipoEmpleado('Antidisturbios');

-- Editar Tipo Empleado
Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codigoTipoEmpleadoIngreso int,in descripcionIngreso varchar(50))
		Begin
			Update TipoEmpleado T
				set T.descripcion = descripcionIngreso
					where T.codigoTipoEmpleado = codigoTipoEmpleadoIngreso;
		End$$
Delimiter ;
call sp_EditarTipoEmpleado(1,'Orden');

-- Eliminar Tipo Empleado
Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codigoTipoEmpleadoIngreso int)
		Begin
			Delete from TipoEmpleado
				where codigoTipoEmpleado = codigoTipoEmpleadoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarTipoEmpleado(1);

-- Listar Tipo Empleados
Delimiter $$
	Create procedure sp_ListarTipoEmpleados()
		Begin
			Select 
				T.codigoTipoEmpleado, 
                T.descripcion
					from TipoEmpleado T;
		End$$
Delimiter ;
call sp_ListarTipoEmpleados();

-- Buscar Tipo Empleado
Delimiter $$
	Create procedure sp_BucarTipoEmpleado(in codigoTipoEmpleadoIngreso int)
		Begin
			Select 
				T.codigoTipoEmpleado, 
                T.descripcion
					from TipoEmpleado T
						where T.codigoTipoEmpleado = codigoTipoEmpleadoIngreso;
        End$$
Delimiter ;
call sp_BucarTipoEmpleado(1);

-- ---------------------------------- TIPO PLATO -------------------------------------------
-- Agregar Tipo Plato
Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descripcionPlato varchar(100))
		Begin
			Insert into TipoPlato (descripcionPlato)
				values (descripcionPlato);
        End$$
Delimiter ;
call sp_AgregarTipoPlato('Almejas de mesopotamia');
call sp_AgregarTipoPlato('Sopa de maiz');
call sp_AgregarTipoPlato('Almejas de mariscos');
call sp_AgregarTipoPlato('Hamburgesas');
call sp_AgregarTipoPlato('Cheasee');
call sp_AgregarTipoPlato('Dedos de pollo');
call sp_AgregarTipoPlato('Pollo enpanizado');
call sp_AgregarTipoPlato('Caldo de pollo');
call sp_AgregarTipoPlato('Dobladas');
call sp_AgregarTipoPlato('Quesadillas');

-- Editar Tipo Plato
Delimiter $$
	Create procedure sp_EditarTipoPlato(in codigoTipoPlatoIngreso int, in descripcionPlatoIngreso varchar(100))
		Begin
			Update TipoPlato T
				set T.descripcionPlato = descripcionPlatoIngreso
					where T.codigoTipoPlato = codigoTipoPlatoIngreso;
        End$$
Delimiter ;
call sp_EditarTipoPlato(1,'Almejar al sol');

-- Eliminar Tipo Plato
Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codigoTipoPlatoIngreso int)
		Begin
			Delete from TipoPlato
				where codigoTipoPlato = codigoTipoPlatoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarTipoPlato(1);

-- Listar Tipo Platos
Delimiter $$
	Create procedure sp_ListarTiposPlatos()
		Begin
			Select
				T.codigoTipoPlato, 
                T.descripcionPlato
					from TipoPlato T;
        End$$
Delimiter ;
call sp_ListarTiposPlatos();

-- Buscar Tipo Plato
Delimiter $$
	Create procedure sp_BuscarTiploPlatos(in codigoTipoPlatoIngreso int)
		Begin
			Select
				T.codigoTipoPlato, 
                T.descripcionPlato
					from TipoPlato T
						where T.codigoTipoPlato = codigoTipoPlatoIngreso;
        End$$

Delimiter ;
call sp_BuscarTiploPlatos(1);

-- ---------------------------------- PRODUCTOS --------------------------------------------
-- Crear Productos
Delimiter $$
    Create procedure sp_CrearProducto(in nombreProducto varchar (150),in catidad int)
        Begin
            Insert into Productos (nombreProducto, catidad)
                values (nombreProducto, catidad);
        End$$
Delimiter ;
call sp_CrearProducto('manzanas',12);
call sp_CrearProducto('pan',16);
call sp_CrearProducto('zanahoria',93);
call sp_CrearProducto('peras',35);
call sp_CrearProducto('queso',85);
call sp_CrearProducto('lechuga',37);
call sp_CrearProducto('maiz',59);
call sp_CrearProducto('mariscos',10);
call sp_CrearProducto('tortillas',1);
call sp_CrearProducto('manzanas',94);

-- Actualizar Productos
Delimiter $$
    Create procedure sp_ActualizarProducto(in codigoProductoIngreso int, in nombreProductoIngreso varchar (150), in catidadIngreso int)
        Begin
            Update Productos P
              Set
                P.nombreProducto = nombreProductoIngreso,
                P.catidad = catidadIngreso
                where P.codigoProducto = codigoProductoIngreso;
        End$$
Delimiter ;
call sp_ActualizarProducto(1,'Pollo',12);

-- Listar Productos
Delimiter $$
    Create procedure sp_ListarProductos()
        Begin
            Select
                P.codigoProducto,
                P.nombreProducto,
                P.catidad
                from Productos P;
        End$$
Delimiter ;
call sp_ListarProductos();

-- Buscar Productos
Delimiter $$
    Create procedure sp_BuscarProducto(in codigoProductoIngreso int)
        Begin
            Select
                P.codigoProducto,
                P.nombreProducto,
                P.catidad
                from Productos P
                    where P.codigoProducto = codigoProductoIngreso;
        End$$
Delimiter ;
call sp_BuscarProducto(1);

-- Eliminar Productos
Delimiter $$
    Create procedure sp_EliminarProductos(in codigoProductoIngreso int)
        Begin
            Delete from Productos
               where codigoProducto = codigoProductoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarProducto(1);

-- ---------------------------------- EMPLEADOS --------------------------------------------
-- Crear Empleado
Delimiter $$
    Create procedure sp_CrearEmpleado(in numeroEmpleado int,in apellidosEmpleado varchar (150),
    in nombresEmpleado varchar (150),in direccionEmpleado varchar (150),
    in telefonoContacto varchar (8),in gradoCocinero varchar (50),in codigoTipoEmpleado int)
        Begin
            Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, 
            direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
                values (numeroEmpleado, apellidosEmpleado, nombresEmpleado, 
                direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado);
        End$$
Delimiter ;
call sp_CrearEmpleado(1,'Alvarez','Fernan','16-47 zona 6 Barrio San Antonio','01247192','Primero',1);
call sp_CrearEmpleado(2,'Hernández','Alejandro','16-47 zona 6 Barrio San Antonio','01247192','Segundo',2);
call sp_CrearEmpleado(3,'García','Miguel Ángel	','16-47 zona 6 Barrio San Antonio','01247192','Tercero',3);
call sp_CrearEmpleado(4,'Martínez','Eduardo','16-47 zona 6 Barrio San Antonio','01247192','Primero',4);
call sp_CrearEmpleado(5,'López ','José Luis','16-47 zona 6 Barrio San Antonio','01247192','Primero',5);
call sp_CrearEmpleado(6,'González ','Juan Carlos','16-47 zona 6 Barrio San Antonio','01247192','Segundo',6);
call sp_CrearEmpleado(7,'Pérez','Daniel','16-47 zona 6 Barrio San Antonio','01247192','Primero',7);
call sp_CrearEmpleado(8,'Rodríguez ','David','16-47 zona 6 Barrio San Antonio','01247192','Quinto',8);
call sp_CrearEmpleado(9,'Sánchez','Ricardo','16-47 zona 6 Barrio San Antonio','01247192','Tercero',9);
call sp_CrearEmpleado(10,'Ramírez','Gabriela','16-47 zona 6 Barrio San Antonio','01247192','Cuarto',10);

-- Actualizar Empleado
Delimiter $$
    Create procedure sp_ActualizarEmpleado(in codigoEmpleadoIngreso int,in numeroEmpleadoIngreso int,
    in apellidosEmpleadoIngreso varchar (150),in nombresEmpleadoIngreso varchar (150),
    in direccionEmpleadoIngreso varchar (150),in telefonoContactoIngreso varchar (8),
    in gradoCocineroIngreso varchar (50))
        Begin
            Update Empleados E
              Set
                E.numeroEmpleado = numeroEmpleadoIngreso,
                E.apellidosEmpleado = apellidosEmpleadoIngreso,
                E.nombresEmpleado = nombresEmpleadoIngreso,
                E.direccionEmpleado = direccionEmpleadoIngreso,
                E.telefonoContacto = telefonoContactoIngreso,
                E.gradoCocinero = gradoCocineroIngreso
                where E.codigoEmpleado = codigoEmpleadoIngreso;
        End$$
Delimiter ;
call sp_ActualizarEmpleado(1,1,'Alvarez','Fernan','16-47 zona 6 Barrio San Antonio','01257839','Segundo');

-- Listar Empleados
Delimiter $$
    Create procedure sp_ListarEmpleados()        
		Begin
            Select
                E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
					from Empleados E;
        End$$
Delimiter ;
call sp_ListarEmpleados();

-- Buscar Empleado
Delimiter $$
    Create procedure sp_BuscarEmpleado(in codigoEmpleadoIngreso int)
        Begin
            Select
                E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
                from Empleados E
                    where E.codigoEmpleado = codigoEmpleadoIngreso;
        End$$
Delimiter ;
call sp_BuscarEmpleado(1);

-- Eliminar Empleado
Delimiter $$

    Create procedure sp_EliminarEmpleado(in codigoEmpleadoIngreso int)
        Begin
            Delete from Empleados
               where codigoEmpleado = codigoEmpleadoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarEmpleado(1);

-- ---------------------------------- SERVICIOS --------------------------------------------
-- Crear Servicio
Delimiter $$
    Create procedure sp_CrearServicio(in fechaServicio date,in tipoServicio varchar (150),
    in horaServicio time,in lugarServicio varchar (150),in telefonoContacto varchar (8),in codigoEmpresa int)
        Begin
            Insert into Servicios (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
                values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End$$
Delimiter ;
call sp_CrearServicio('2022-03-05','Atencion al cliente','08:30:00','En plaza central','12395012',1);
call sp_CrearServicio('2010-05-13','Fiesta','10:30:00','En plaza Barrios','30065783',2);
call sp_CrearServicio('2019-08-03','Pijamada','10:00:00','En portales','52331076',3);
call sp_CrearServicio('2023-02-18','Reunion','09:00:00','En San Martin','57046633',4);
call sp_CrearServicio('2022-08-02','Reunion','02:00:00','En Plaza Reyes','73488067',5);
call sp_CrearServicio('2021-03-06','Fiesta','11:00:00','En plaza del norte','68603921',6);
call sp_CrearServicio('2018-04-16','Reunion Familiar','12:00:00','En Barrio San Antonio','97860317',7);
call sp_CrearServicio('2019-01-24','Fiesta','01:30:00','En Cipresales','80947460',8);
call sp_CrearServicio('2021-05-29','Reunion','08:00:00','En San Angel','87559883',9);
call sp_CrearServicio('2023-12-10','Pijamada','06:30:00','En Diagonal 12','88710921',10);

-- Actualizar Servicio
Delimiter $$
    Create procedure sp_ActualizarServicio(in codigoServicioIngreso int,in fechaServicioIngreso date,
    in tipoServicioIngreso varchar (150),in horaServicioIngreso time,in lugarServicioIngreso varchar (150),
    in telefonoContactoIngreso varchar (8))
        Begin
            Update Servicios S
              Set
                S.fechaServicio = fechaServicioIngreso,
                S.tipoServicio = tipoServicioIngreso,
                S.horaServicio = horaServicioIngreso,
                S.lugarServicio = lugarServicioIngreso,
                S.telefonoContacto = telefonoContactoIngreso
                where S.codigoServicio = codigoServicioIngreso;
        End$$
Delimiter ;
call sp_ActualizarServicio(1,'2022-03-05','Atencion al Personal','08:30:00','En plaza barrios','12395012');

-- Listar Servicios
Delimiter $$
    Create procedure sp_ListarServicios()
        Begin
            Select
                S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
                    from Servicios S;
        End$$
Delimiter ;
call sp_ListarServicios();

-- Buscar Servicio
Delimiter $$
    Create procedure sp_BuscarServicio(in codigoServicioIngreso int)
        Begin
            Select
                S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
                from Servicios S
                    where S.codigoServicio = codigoServicioIngreso;
        End$$
Delimiter ;
call sp_BuscarServicio(1);

-- Eliminar Servicio
Delimiter $$

    Create procedure sp_EliminarServicio(in codigoServicioIngreso int)
        Begin
            Delete from Servicios
               where codigoServicio = codigoServicioIngreso;
        End$$
Delimiter ;
-- call sp_EliminarServicio(1);

-- ---------------------------------- PRESUPUESTO ------------------------------------------
-- Crear Presupuesto
Delimiter $$
    Create procedure sp_CrearPresupuesto(in fechaSolicitud date,in cantidadPresupuesto decimal (10,2),in codigoEmpresa int)
        Begin
            Insert into Presupuesto (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
                values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End$$
Delimiter ;
call sp_CrearPresupuesto('2022-12-13',10000,1);
call sp_CrearPresupuesto('2023-04-23',5006.54,2);
call sp_CrearPresupuesto('2023-07-14',8665.85,3);
call sp_CrearPresupuesto('2023-05-01',93625.55,4);
call sp_CrearPresupuesto('2022-10-02',91066.48,5);
call sp_CrearPresupuesto('2022-02-23',54330.55,6);
call sp_CrearPresupuesto('2021-08-20',8361.46,7);
call sp_CrearPresupuesto('2021-06-29',12529.74,8);
call sp_CrearPresupuesto('2021-03-19',12529.74,9);
call sp_CrearPresupuesto('2023-01-30',15286.26,10);

-- Actualizar Presupuesto
Delimiter $$ 
	Create procedure sp_ActualizarPresupuesto(in codigoPresupuestoIngreso int,in fechaSolicitudIngreso date,
    in cantidadPresupuestoIngreso decimal (10,2))
        Begin
            Update Presupuesto P
              Set
                P.fechaSolicitud = fechaSolicitudIngreso,
                P.cantidadPresupuesto = cantidadPresupuestoIngreso
                where P.codigoPresupuesto = codigoPresupuestoIngreso;
        End$$
Delimiter ;
call sp_ActualizarPresupuesto(1,'2022-12-20',10001);

-- Listar Presupuestos
Delimiter $$
    Create procedure sp_ListarPresupuestos()
        Begin
            Select
                P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                P.codigoEmpresa
                    from Presupuesto P;
        End$$
Delimiter ;
call sp_ListarPresupuestos();

-- Buscar Presupuesto
Delimiter $$
    Create procedure sp_BuscarPresupuesto(in codigoPresupuestoIngreso int)
        Begin
            Select
                P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                P.codigoEmpresa
                from Presupuesto P
                    where P.codigoPresupuesto = codigoPresupuestoIngreso;
        End$$
Delimiter ;
call sp_BuscarPresupuesto(1);

-- Eliminar Presupuesto
Delimiter $$
    Create procedure sp_EliminarPresupuesto(in codigoPresupuestoIngreso int)
        Begin
            Delete from Presupuesto
               where codigoPresupuesto = codigoPresupuestoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarPresupuesto(1);

-- ---------------------------------- PLATOS -----------------------------------------------
-- Crear Plato
Delimiter $$
    Create procedure sp_CrearPlato(in cantidadPlato int,in nombrePlato varchar (50),
    in descripcionPlato varchar (150),in precioPlato decimal (10,2),in codigoTipoPlato int)
        Begin
            Insert into Platos (cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
                values (cantidadPlato, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End$$
Delimiter ; 
call sp_CrearPlato(50,'Pizza','Pizza de queso con piña',25,1);
call sp_CrearPlato(71,'Hamburgesa','Hamburgesas de toro',10,2);
call sp_CrearPlato(38,'Pizza Vegana','Pizza vegana',79,3);
call sp_CrearPlato(5,'Obleas','Obleas',90,4);
call sp_CrearPlato(92,'Doblada','Con queso',42,5);
call sp_CrearPlato(77,'Caldo de res','Caldo de res sasonado',84,6);
call sp_CrearPlato(21,'Caldo de pollo','Caldo de pollo marinado',71,7);
call sp_CrearPlato(38,'Pan con jamon','Pan con jamon y lechuga',21,8);
call sp_CrearPlato(68,'Shucos','Shucos que estan limpios',58,9);
call sp_CrearPlato(34,'Pan con salchica','salchichapapa',64,10);
-- Actualizar Plato
Delimiter $$
    Create procedure sp_ActualizarPlato(in codigoPlatoIngreso int,in cantidadIngreso int,
    in nombrePlatoIngreso varchar (50),in descripcionPlatoIngreso varchar (150),
    in precioPlatoIngreso decimal (10,2))
        Begin
            Update Platos P
              Set
                P.cantidadPlato = cantidadIngreso,
                P.nombrePlato = nombrePlatoIngreso,
                P.descripcionPlato = descripcionPlatoIngreso,
                P.precioPlato = precioPlatoIngreso
                where P.codigoPlato = codigoPlatoIngreso;
        End$$
Delimiter ;	
call sp_ActualizarPlato(1,20,'Espagueti Voloneso','Espagueti con albondigas...',26);

-- Listar Platos
Delimiter $$
    Create procedure sp_ListarPlatos()
        Begin
            Select
                P.codigoPlato,
                P.cantidadPlato,
                P.nombrePlato,
                P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
                    from Platos P;
        End$$
Delimiter ;
call sp_ListarPlatos();

-- Buscar Plato
Delimiter $$
    Create procedure sp_BuscarPlato(in codigoPlatoIngreso int)
        Begin
            Select
                P.codigoPlato,
                P.cantidadPlato,
                P.nombrePlato,
                P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
                from Platos P
                    where P.codigoPlato = codigoPlatoIngreso;
        End$$
Delimiter ;
call sp_BuscarPlato(1);

-- Eliminar Plato
Delimiter $$

    Create procedure sp_EliminarPlato(in codigoPlatoIngreso int)
        Begin
            Delete from Platos 
               where codigoPlato = codigoPlatoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarPlato(1);

-- ---------------------------------- PRODUCTOS HAS PLATOS ---------------------------------
-- Crear Producto_has_Plato
Delimiter $$
    Create procedure sp_CrearProductos_has_Plato(in codigoPlato int,in codigoProducto int)
        Begin
            Insert into Productos_has_Platos (codigoPlato, codigoProducto)
                values (codigoPlato, codigoProducto);
        End$$
Delimiter ;
call sp_CrearProductos_has_Plato(1,1);
call sp_CrearProductos_has_Plato(2,2);
call sp_CrearProductos_has_Plato(3,3);
call sp_CrearProductos_has_Plato(4,4);
call sp_CrearProductos_has_Plato(5,5);
call sp_CrearProductos_has_Plato(6,6);
call sp_CrearProductos_has_Plato(7,7);
call sp_CrearProductos_has_Plato(8,8);
call sp_CrearProductos_has_Plato(9,9);
call sp_CrearProductos_has_Plato(10,10);

-- Actualizar Producto_has_Plato
-- Delimiter $$
--    Create procedure sp_ActualizarProductos_has_Plato(in Productos_codigoProductoIngreso int,
--    in codigoPlatoIngreso int)
--        Begin
--            Update Productos_has_Platos P
--              Set
--                P.codigoPlato = codigoPlatoIngreso,
--                P.codigoProducto = codigoProductoIngreso
--                where P.Productos_codigoProducto = Productos_codigoProductoIngreso;
--        End$$
-- Delimiter ;
-- call sp_ActualizarProductos_has_Plato(1,2,1);

-- Listar Productos_has_Platos
Delimiter $$
    Create procedure sp_ListarProductos_has_Platos()
        Begin
            Select
                P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                    from Productos_has_Platos P;
        End$$
Delimiter ;
call sp_ListarProductos_has_Platos();

-- Buscar Producto_has_Plato
Delimiter $$
    Create procedure sp_BuscarProductos_has_Plato(in Productos_codigoProductoIngreso int)
        Begin
            Select
                P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                from Productos_has_Platos P
                    where P.Productos_codigoProducto = Productos_codigoProductoIngreso;
        End$$
Delimiter ;
call sp_BuscarProductos_has_Plato(1);

-- Eliminar Producto_has_Plato
Delimiter $$
    Create procedure sp_EliminarProductos_has_Plato(in Productos_codigoProductoIngreso int)
        Begin
            Delete from Productos_has_Platos
               where Productos_codigoProducto = Productos_codigoProductoIngreso;
        End$$
Delimiter ;
-- call sp_EliminarProductos_has_Plato(1);

-- ---------------------------------- SERVICIOS HAS PLATOS ---------------------------------
-- Crear Servicios_has_Plato
Delimiter $$
    Create procedure sp_CrearServicios_has_Plato(in codigoPlato int,in codigoServicio int)
        Begin
            Insert into Servicios_has_Platos (codigoPlato, codigoServicio)
                values (codigoPlato, codigoServicio);
        End$$
Delimiter ;
call sp_CrearServicios_has_Plato(1,1);
call sp_CrearServicios_has_Plato(2,2);
call sp_CrearServicios_has_Plato(3,3);
call sp_CrearServicios_has_Plato(4,4);
call sp_CrearServicios_has_Plato(5,5);
call sp_CrearServicios_has_Plato(6,6);
call sp_CrearServicios_has_Plato(7,7);
call sp_CrearServicios_has_Plato(8,8);
call sp_CrearServicios_has_Plato(9,9);
call sp_CrearServicios_has_Plato(10,10);

-- Actualizar Servicios_has_Plato
-- Delimiter $$
--    Create procedure sp_ActualizarServicios_has_Plato(in Servicios_codigoServicioIngreso int,
--    in codigoPlatoIngreso int,in codigoServicioIngreso int)
--        Begin
--            Update Servicios_has_Platos S
--              Set
--                S.codigoPlato = codigoPlatoIngreso,
--                S.codigoServicio = codigoServicioIngreso
--                where S.Servicios_codigoServicio = Servicios_codigoServicioIngreso;
--        End$$
-- Delimiter ;
-- call sp_ActualizarServicios_has_Plato(1,1,2);

-- Listar Servicios_has_Platos
Delimiter $$
    Create procedure sp_ListarServicios_has_Platos()
        Begin
            Select
                S.Servicios_codigoServicio,
                S.codigoPlato,
                S.codigoServicio
                    from Servicios_has_Platos S;
        End$$
Delimiter ;
call sp_ListarServicios_has_Platos();

-- Buscar Servicios_has_Plato
Delimiter $$
    Create procedure sp_BuscarServicios_has_Plato(in Servicios_codigoServicioIngreso int)
        Begin
            Select
                S.Servicios_codigoServicio,
                S.codigoPlato,
                S.codigoServicio
                from Servicios_has_Platos S
                    where S.Servicios_codigoServicio = Servicios_codigoServicioIngreso;
        End$$
Delimiter ;
call sp_BuscarServicios_has_Plato(1);

-- Eliminar Servicios_has_Plato
Delimiter $$
    Create procedure sp_EliminarServicios_has_Plato(in Servicios_codigoServicioIngreso int)
        Begin
            Delete from Servicios_has_Platos
               where Servicios_codigoServicio = Servicios_codigoServicioIngreso;
        End$$
Delimiter ;
-- call sp_EliminarServicios_has_Plato(1);

-- ---------------------------------- SERVICIOS HAS EMPLEADOS ------------------------------
-- Crear Servicios_has_Empleado
Delimiter $$
    Create procedure sp_CrearServicios_has_Empleado(in codigoServicio int,
    in codigoEmpleado int,in fechaEvento date,in horaEvento time,in lugarEvento varchar(150))
        Begin
            Insert into Servicios_has_Empleados ( codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
                values ( codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End$$
Delimiter ;
call sp_CrearServicios_has_Empleado(1,1,'2022-12-20','10:34:00','Parroquia central');
call sp_CrearServicios_has_Empleado(2,2,'2023-01-23','10:20:00','Parroquia central');
call sp_CrearServicios_has_Empleado(3,3,'2023-04-10','15:50:00','Parroquia central');
call sp_CrearServicios_has_Empleado(4,4,'2021-06-18','17:20:00','Parroquia central');
call sp_CrearServicios_has_Empleado(5,5,'2021-10-19','19:10:00','Parroquia central');
call sp_CrearServicios_has_Empleado(6,6,'2024-02-20','13:40:00','Parroquia central');
call sp_CrearServicios_has_Empleado(7,7,'2024-10-02','05:30:00','Parroquia central');
call sp_CrearServicios_has_Empleado(8,8,'2022-05-01','02:25:00','Parroquia central');
call sp_CrearServicios_has_Empleado(9,9,'2023-03-18','10:30:00','Parroquia central');
call sp_CrearServicios_has_Empleado(10,10,'2021-07-13','22:10:00','Parroquia central');

-- Actualizar Servicios_has_Empleado
 Delimiter $$
    Create procedure sp_ActualizarServicios_has_Empleado(in Servicios_codigoServicioIngreso int,
    in fechaEventoIngreso date, in horaEventoIngreso time,in lugarEventoIngreso varchar (150))
        Begin
            Update Servicios_has_Empleados S
              Set
                S.fechaEvento = fechaEventoIngreso,
                S.horaEvento = horaEventoIngreso,
                S.lugarEvento = lugarEventoIngreso
                where S.Servicios_codigoServicio = Servicios_codigoServicioIngreso;
        End$$
Delimiter ;
call sp_ActualizarServicios_has_Empleado(1,'2023-05-20','21:20:12','Parroquia colonial');

-- Listar Servicios_has_Empleados
Delimiter $$
    Create procedure sp_ListarServicios_has_Empleados()
        Begin
            Select
                S.Servicios_codigoServicio,
                S.codigoServicio,
                S.codigoEmpleado,
                S.fechaEvento,
                S.horaEvento,
                S.lugarEvento
                    from Servicios_has_Empleados S;
        End$$
Delimiter ;
call sp_ListarServicios_has_Empleados();

-- Buscar Servicios_has_Empleado
Delimiter $$
    Create procedure sp_BuscarServicios_has_Empleado(in Servicios_codigoServicioIngreso int)
        Begin
            Select
                S.Servicios_codigoServicio,
                S.codigoServicio,
                S.codigoEmpleado,
                S.fechaEvento,
                S.horaEvento,
                S.lugarEvento
                from Servicios_has_Empleados S
                    where S.Servicios_codigoServicio = Servicios_codigoServicioIngreso;
        End$$
Delimiter ;
call sp_BuscarServicios_has_Empleado(1);

-- Eliminar Servicios_has_Empleado
Delimiter $$
    Create procedure sp_EliminarServicios_has_Empleado(in Servicios_codigoServicioIngreso int)
        Begin
            Delete from Servicios_has_Empleados
               where Servicios_codigoServicio = Servicios_codigoServicioIngreso;
        End$$
Delimiter ;
-- call sp_EliminarServicios_has_Empleado(1);
-- ---------------------------------- USUARIO ----------------------------------
-- Crear Usuario
Delimiter $$
    Create procedure sp_CrearUsuario(in nombreUsuario varchar(100),in apellidoUsuario varchar(100), 
		in usuarioLogin varchar(50),in contrasena varchar(50))
        Begin
            Insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
                values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
        End$$
Delimiter ;
call sp_CrearUsuario('1','1','1','1');
call sp_CrearUsuario('Pedro','Armas','parmas','parmas');

-- Listar Usuarios
Delimiter $$
    Create procedure sp_ListarUsuarios()
        Begin
            Select
				U.codigoUsuario, 
                U.nombreUsuario, 
                U.apellidoUsuario, 
                U.usuarioLogin, 
                U.contrasena
					from Usuario U;
        End$$
Delimiter ;

call sp_ListarUsuarios();

Delimiter $$
	Create procedure sp_ReporteEmpresa(in codigoEmpresaIngreso int)
    Begin
		Select 
			*
            from Empresas E 
            inner join Presupuesto P on E.codigoEmpresa = P.codigoEmpresa
            inner join Servicios S on P.codigoEmpresa = S.codigoEmpresa
            inner join Servicios_has_Empleados SE on S.codigoServicio = SE.codigoServicio
            inner join Empleados Em on SE.codigoEmpleado = Em.codigoEmpleado
            inner join TipoEmpleado TE on Em.codigoTipoEmpleado = TE.codigoTipoEmpleado
            inner join Servicios_has_Platos SP on S.codigoServicio = SP.codigoServicio
            inner join Platos PT on SP.codigoPlato = PT.codigoPlato
            inner join TipoPlato TP on PT.codigoTipoPlato = TP.codigoTipoPlato
            inner join Productos_has_Platos PP on PT.codigoPlato = PP.codigoPlato
            inner join Productos Pd on PP.codigoProducto = Pd.codigoProducto
				where E.codigoEmpresa = codigoEmpresaIngreso;
	End $$
Delimiter ;

call sp_ReporteEmpresa(1);

select * from Servicios_has_Empleados;







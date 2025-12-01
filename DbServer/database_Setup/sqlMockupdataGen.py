import random

# Configuración
NUM_USERS = 100
NUM_DIRECCIONES = 200
NUM_ENVIOS = 200  # Ajustar para llegar al total deseado
FILE_NAME = "datos_prueba.sql"

cities = ["Quito", "Guayaquil", "Cuenca", "Ambato", "Manta", "Loja", "Machala", "Ibarra"]
streets = ["Av. Amazonas", "Calle Larga", "Av. 9 de Octubre", "Av. Patria", "Calle 10 de Agosto", "Av. Shyris", "Calle Simon Bolivar"]
names = ["Juan", "Maria", "Pedro", "Luis", "Ana", "Sofia", "Carlos", "Jose", "Fernanda", "Gabriela"]
last_names = ["Perez", "Gomez", "Lopez", "Rodriguez", "Silva", "Martinez", "Sanchez", "Romero", "Vargas", "Mendoza"]
paquete_types = ["Documentos", "Caja Pequena", "Caja Grande", "Electrodomestico"]

sql = []
sql.append("SET FOREIGN_KEY_CHECKS=0;")

# 1. Ciudades
sql.append("-- Ciudades")
for city in cities:
    x = random.uniform(-80, -78)
    y = random.uniform(-4, 1)
    sql.append(f"INSERT INTO ciudades (nombre_ciudad, coordenada_ciudad_x, coordenada_ciudad_y) VALUES ('{city}', {x:.8f}, {y:.8f});")

# 2. Direcciones
sql.append("-- Direcciones")
direccion_ids = list(range(1, NUM_DIRECCIONES + 1))
for i in direccion_ids:
    city = random.choice(cities)
    main = random.choice(streets)
    sec = f"Calle {random.randint(1, 100)}"
    num = f"N{random.randint(10, 99)}-{random.randint(100, 999)}"
    sql.append(f"INSERT INTO direcciones (id_direccion, ciudades_nombre_ciudad, calle_principal, calle_secundaria, numero_edificacion, detalle) VALUES ({i}, '{city}', '{main}', '{sec}', '{num}', 'Detalle generico');")

# 3. Usuarios
sql.append("-- Usuarios")
all_cedulas = []
for i in range(NUM_USERS):
    cedula = f"{random.randint(1000000000, 1999999999)}"
    while cedula in all_cedulas: # Evitar duplicados
        cedula = f"{random.randint(1000000000, 1999999999)}"
    all_cedulas.append(cedula)
    
    fname = random.choice(names)
    lname = random.choice(last_names)
    email = f"{fname.lower()}.{lname.lower()}.{i}@test.com"
    addr = random.choice(direccion_ids)
    sql.append(f"INSERT INTO usuarios (cedula, nombre, apellidos, correo, contrasena, id_direccion_principal) VALUES ('{cedula}', '{fname}', '{lname}', '{email}', 'pass123', {addr});")

# Roles
empresarios = all_cedulas[:10]
transportistas = all_cedulas[10:30]

# 4. Empresarios y Companias
sql.append("-- Empresarios y Companias")
sucursales_keys = [] # (id_direccion, RUC)
companias_ruc = []

for i, ced in enumerate(empresarios):
    sql.append(f"INSERT INTO empresarios (usuario_cedula, cargo_empresa) VALUES ('{ced}', 'Gerente');")
    ruc = f"{ced}001"
    companias_ruc.append(ruc)
    sql.append(f"INSERT INTO companias (RUC, empresario_cedula, nombre_compania) VALUES ('{ruc}', '{ced}', 'Empresa {i}');")
    
    # Crear Sucursal
    addr = random.choice(direccion_ids)
    sql.append(f"INSERT INTO sucursales (id_direccion, compania_RUC) VALUES ({addr}, '{ruc}');")
    sucursales_keys.append((addr, ruc))

# 5. Transportistas y Vehiculos
sql.append("-- Transportistas, Modelos y Vehiculos")
sql.append("INSERT INTO modelos (nombre_modelo, maximo_peso) VALUES ('Camion', 5000), ('Moto', 50);")

for ced in transportistas:
    sql.append(f"INSERT INTO transportistas (usuario_cedula, numero_licencia) VALUES ('{ced}', '{ced}-LIC');")
    modelo = random.choice(['Camion', 'Moto'])
    sql.append(f"INSERT INTO vehiculos (transportista_cedula, nombre_modelo, placa) VALUES ('{ced}', '{modelo}', 'ABC-{random.randint(100,999)}');")

# 6. Paquetes
sql.append("-- Caracteristicas y Paquetes")
for pt in paquete_types:
    sql.append(f"INSERT INTO caracteristicas_paquete (tipo_paquete) VALUES ('{pt}');")

paquete_ids = []
for i in range(1, NUM_ENVIOS + 1):
    ptype = random.choice(paquete_types)
    sql.append(f"INSERT INTO paquetes (id_paquete, descripcion, peso, tipo) VALUES ({i}, 'Paquete {i}', {random.uniform(1, 20):.2f}, '{ptype}');")
    paquete_ids.append(i)

# 7. Envios
sql.append("-- Envios")
for i in range(1, NUM_ENVIOS + 1):
    suc = random.choice(sucursales_keys)
    dest = random.choice(direccion_ids)
    trans = random.choice(transportistas)
    paq = paquete_ids[i-1]
    
    sql.append(f"INSERT INTO envios (sucursal_RUC, sucursal_id_direccion, id_direccion_entrega, transportista_cedula, id_paquete, tarifa, estado) VALUES ('{suc[1]}', {suc[0]}, {dest}, '{trans}', {paq}, {random.uniform(5, 50):.2f}, 'Entregado');")

sql.append("SET FOREIGN_KEY_CHECKS=1;")

# Guardar archivo
with open(FILE_NAME, "w", encoding="utf-8") as f:
    f.write("\n".join(sql))

print(f"Archivo {FILE_NAME} generado exitosamente con {len(sql)} sentencias.")
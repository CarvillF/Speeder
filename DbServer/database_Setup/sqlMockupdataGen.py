import random
from faker import Faker

fake = Faker('es_ES') # Datos en español

# Configuración de cantidad de datos
NUM_CIUDADES = 10
NUM_DIRECCIONES = 200
NUM_USUARIOS = 100
NUM_EMPRESARIOS = 20
NUM_TRANSPORTISTAS = 20
NUM_COMPANIAS = 15
NUM_SUCURSALES = 15
NUM_PAQUETES = 150
NUM_ENVIOS = 200 # Total aproximado de registros > 500

file_path = 'populate_schema.sql'

def escape_sql(text):
    if text is None: return "NULL"
    return "'" + str(text).replace("'", "''") + "'"

with open(file_path, 'w', encoding='utf-8') as f:
    f.write("USE `SchemaSpeeder`;\n\n")
    
    # 1. CIUDADES
    ciudades = ['Quito', 'Guayaquil', 'Cuenca', 'Santo Domingo', 'Machala', 'Durán', 'Manta', 'Portoviejo', 'Loja', 'Ambato']
    f.write("-- Ciudades\n")
    for ciudad in ciudades:
        lat = fake.latitude()
        lon = fake.longitude()
        f.write(f"INSERT IGNORE INTO ciudades VALUES ('{ciudad}', {lat}, {lon});\n")
    f.write("\n")

    # 2. DIRECCIONES
    f.write("-- Direcciones\n")
    direccion_ids = []
    for i in range(1, NUM_DIRECCIONES + 1):
        ciudad = random.choice(ciudades)
        calle1 = fake.street_name()
        calle2 = fake.street_name()
        num = fake.building_number()
        detalle = fake.secondary_address()
        f.write(f"INSERT INTO direcciones (ciudades_nombre_ciudad, calle_principal, calle_secundaria, numero_edificacion, detalle) VALUES ('{ciudad}', '{calle1}', '{calle2}', '{num}', '{detalle}');\n")
        direccion_ids.append(i)
    f.write("\n")

    # 3. USUARIOS
    f.write("-- Usuarios\n")
    cedulas = []
    for _ in range(NUM_USUARIOS):
        cedula = str(fake.unique.random_number(digits=10, fix_len=True))
        nombre = fake.first_name()
        apellido = fake.last_name()
        correo = fake.unique.email()
        password = "password123" # En prod debe ser hash
        telefono = fake.phone_number()[:15]
        id_dir = random.choice(direccion_ids)
        
        f.write(f"INSERT INTO usuarios (cedula, nombre, apellidos, correo, contrasena, numero_telefono, id_direccion_principal) VALUES ('{cedula}', '{nombre}', '{apellido}', '{correo}', '{password}', '{telefono}', {id_dir});\n")
        cedulas.append(cedula)
    f.write("\n")

    # 4. ROLES (Empresarios y Transportistas)
    random.shuffle(cedulas)
    cedulas_empresarios = cedulas[:NUM_EMPRESARIOS]
    cedulas_transportistas = cedulas[NUM_EMPRESARIOS:NUM_EMPRESARIOS+NUM_TRANSPORTISTAS]
    
    f.write("-- Empresarios\n")
    for ced in cedulas_empresarios:
        f.write(f"INSERT INTO empresarios (usuario_cedula, cargo_empresa, correo_empresarial) VALUES ('{ced}', 'Gerente', '{fake.company_email()}');\n")

    f.write("-- Transportistas\n")
    for ced in cedulas_transportistas:
        f.write(f"INSERT INTO transportistas (usuario_cedula, numero_licencia, tipo_licencia, zona_cobertura, disponibilidad) VALUES ('{ced}', '{fake.bothify(text='LIC-#####')}', 'Tipo E', 'General', 'Disponible');\n")
    f.write("\n")

    # 5. COMPANIAS y SUCURSALES
    f.write("-- Companias\n")
    rucs = []
    lista_rucs_sucursales = [] # Pares (RUC, id_direccion)
    
    for _ in range(NUM_COMPANIAS):
        ruc = str(fake.unique.random_number(digits=13, fix_len=True))
        emp_ced = random.choice(cedulas_empresarios)
        nombre = fake.company()
        desc = fake.catch_phrase()[:100]
        f.write(f"INSERT INTO companias (RUC, empresario_cedula, nombre_compania, tipo_compania, descripcion) VALUES ('{ruc}', '{emp_ced}', '{escape_sql(nombre)[1:-1]}', 'Privada', '{escape_sql(desc)[1:-1]}');\n")
        rucs.append(ruc)

    f.write("-- Sucursales\n")
    # Aseguramos al menos una sucursal por compañia
    used_dirs_sucursal = set()
    for ruc in rucs:
        id_dir = random.choice(direccion_ids)
        if id_dir not in used_dirs_sucursal:
            f.write(f"INSERT INTO sucursales (id_direccion, compania_RUC, activa) VALUES ({id_dir}, '{ruc}', 1);\n")
            lista_rucs_sucursales.append((ruc, id_dir))
            used_dirs_sucursal.add(id_dir)
    f.write("\n")

    # 6. MODELOS y VEHICULOS
    f.write("-- Modelos y Vehiculos\n")
    modelos = ['Van Cargo', 'Camión 3T', 'Moto Delivery', 'Camioneta 4x4']
    for mod in modelos:
        f.write(f"INSERT IGNORE INTO modelos (nombre_modelo, maximo_peso) VALUES ('{mod}', {random.uniform(50, 5000)});\n")
    
    for ced in cedulas_transportistas:
        mod = random.choice(modelos)
        placa = fake.bothify(text='??-####').upper()
        f.write(f"INSERT INTO vehiculos (transportista_cedula, nombre_modelo, color, placa) VALUES ('{ced}', '{mod}', '{fake.color_name()}', '{placa}');\n")

    # 7. PAQUETES
    f.write("-- Paquetes\n")
    tipos_paquete = ['Caja Pequeña', 'Caja Grande', 'Sobre', 'Pallet']
    for tp in tipos_paquete:
        f.write(f"INSERT IGNORE INTO caracteristicas_paquete (tipo_paquete) VALUES ('{tp}');\n")
    
    paquete_ids = []
    for i in range(1, NUM_PAQUETES + 1):
        tipo = random.choice(tipos_paquete)
        desc = fake.sentence()
        peso = round(random.uniform(0.5, 50.0), 2)
        f.write(f"INSERT INTO paquetes (descripcion, peso, tipo) VALUES ('{desc}', {peso}, '{tipo}');\n")
        paquete_ids.append(i)
    f.write("\n")

    # 8. ENVIOS
    f.write("-- Envios\n")
    estados = ['En recogida', 'En camino', 'Entregado', 'Cancelado']
    for _ in range(NUM_ENVIOS):
        sucursal = random.choice(lista_rucs_sucursales) # Tupla (ruc, id_dir)
        suc_ruc = sucursal[0]
        suc_dir = sucursal[1]
        
        id_dest = random.choice(direccion_ids)
        transportista = random.choice(cedulas_transportistas)
        paquete = random.choice(paquete_ids)
        tarifa = round(random.uniform(5.00, 100.00), 2)
        estado = random.choice(estados)
        
        f.write(f"INSERT INTO envios (sucursal_RUC, sucursal_id_direccion, id_direccion_entrega, transportista_cedula, id_paquete, tarifa, estado) VALUES ('{suc_ruc}', {suc_dir}, {id_dest}, '{transportista}', {paquete}, {tarifa}, '{estado}');\n")

print(f"Archivo '{file_path}' generado exitosamente con datos de prueba.")
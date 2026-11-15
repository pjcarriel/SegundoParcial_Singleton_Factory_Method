# Guía de exposición y GitHub — Grupo 1

## 1. Distribución real de los seis integrantes

| Estudiante | Rol | Rama Git sugerida | Entregable |
|---|---|---|---|
| 1 | Teoría Singleton | `docs/teoria-singleton` | Analogía, propósito, problemas que resuelve |
| 2 | Teoría Factory Method | `docs/teoria-factory` | Analogía de fábrica y diferencia con `new` en el cliente |
| 3 | Arquitectura Singleton | `docs/uml-singleton` | Diagrama y explicación de instancia gestora de conexiones |
| 4 | Arquitectura Factory Method | `docs/uml-factory-method` | Diagrama creador/producto y aplicaciones empresariales |
| 5 | Desarrollo Singleton | `feature/singleton-mysql` | `ConexionBaseDatos`, demostración 5 referencias |
| 6 | Desarrollo Factory Method | `feature/factory-alertas` | Interfaz, productos, fábricas, validaciones |

La integración del menú, repositorio JDBC y pruebas puede hacerse en una rama adicional `feature/integracion-consola` con **commits reales** de quienes participaron. Cada estudiante debe trabajar en su propia rama, no recrear contribuciones ficticias.

## 2. Orden recomendado en clase

1. **Estudiante 5:** abrir `ConexionBaseDatos`, explicar `private static final`, constructor privado y `getInstancia()`; ejecutar **opción 4** y comprobar las cinco referencias con `==`.
2. **Estudiante 6:** mostrar `IAlerta`, `FabricaAlerta` y dos fábricas concretas; ejecutar **opción 5**, que crea ACCESO, RED y SERVIDOR sin MySQL.
3. **Ambos:** ejecutar **opción 6** para confirmar MySQL y **opción 1** para guardar una alerta (Factory Method + Singleton en un mismo flujo).
4. **Ambos:** ejecutar **opción 2** para ver el historial persistido y **opción 3** para revisar las estadísticas.
5. **Cierre:** enseñar los diagramas y el historial de commits/PR en GitHub. Mencionar SRP, OCP, LSP, ISP y DIP con clases concretas.

## 3. Comandos Git (ejecutar en la carpeta raíz)

Si el directorio todavía **no es** un repositorio:

```bat
git init -b main
git add .gitignore pom.xml README.md config/database.properties.example
git commit -m "Inicializa proyecto Java de consola y dependencias Maven"
```

Para que el estudiante 5 trabaje en su rama:

```bat
git switch main
git switch -c feature/singleton-mysql
git add src/main/java/ec/edu/espe/soc/singleton/ConexionBaseDatos.java
git commit -m "Implementa Singleton para gestor MySQL y HikariCP"
```

Para que el estudiante 6 trabaje en su rama **desde `main`**, después de haber creado la base del proyecto:

```bat
git switch main
git switch -c feature/factory-alertas
git add src/main/java/ec/edu/espe/soc/modelo src/main/java/ec/edu/espe/soc/factory
git commit -m "Implementa Factory Method y productos IAlerta"
```

Conecta el repositorio remoto, después de crearlo en GitHub y reemplazar los marcadores:

```bat
git remote add origin https://github.com/USUARIO/REPOSITORIO.git
git push -u origin main
```

En cada rama:

```bat
git push -u origin feature/factory-alertas
```

Realizar **Pull Request** a `main`, revisar los cambios y hacer **Merge**. Si un estudiante integra los archivos de consola y JDBC, que lo haga en `feature/integracion-consola`, no directamente en `main`.

> **Importante:** entregar este ZIP como punto de partida no equivale a participación individual. Si el docente revisa ramas y commits, es mejor que cada persona realice modificaciones y pruebas auténticas que reflejen su trabajo. Nunca subir el archivo `config/database.properties` con credenciales.

## 4. Guion corto del Live Coding

**Estudiante 5:** «El constructor privado impide que otras clases construyan gestores de conexiones. `getInstancia()` devuelve siempre el mismo objeto. Lo solicitamos cinco veces: los identityHashCode coinciden y `==` confirma la misma referencia. Este objeto gestiona un pool de conexiones JDBC, no una única conexión física compartida».

**Estudiante 6:** «`IAlerta` es el producto abstracto. `FabricaAlerta` define `crearAlerta()` y sus subclases lo implementan. Si recibimos el texto RED, elegimos la fábrica RED y obtenemos `AlertaRed` sin que el código cliente deba llamar directamente a su constructor. Ahora guardamos la alerta en MySQL para demostrar la integración».

**Ambos:** «Los patrones no prohíben `new`: controlan dónde ocurre la creación. La aplicación también aplica SOLID gracias a las interfaces, las responsabilidades separadas y el acceso a datos desacoplado».

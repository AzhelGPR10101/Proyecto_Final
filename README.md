# Krypton

Sistema integral de gestión de inventario y facturación comercial para microempresas de Cuenca, desarrollado con Programación Orientada a Objetos (Java) bajo metodología Scrum.

## Descripción del proyecto

En Cuenca, las microempresas y pequeños negocios locales suelen depender de métodos manuales, anotaciones físicas u hojas de cálculo no estructuradas para su control operativo. Esto genera pérdidas de stock, sistemas rígidos que no se adaptan a distintos giros de negocio, y una toma de decisiones basada en intuición en lugar de datos.

**Krypton** es una aplicación de escritorio (Java + JavaFX) que resuelve esto mediante:

- **Motor de plantillas maleables**: permite definir campos y categorías personalizadas en tiempo de ejecución, adaptando el sistema a cualquier tipo de negocio sin reconfiguración costosa.
- **Gestión avanzada de productos**: CRUD completo con marcas, precios, existencias, estados críticos de stock y galerías de imágenes.
- **Facturación automatizada**: generación de comprobantes en PDF conforme a normativas del SRI, con envío automático al correo del cliente.
- **Smart-Auditing**: monitor en segundo plano (hilos de Java) que alerta proactivamente sobre vencimientos próximos o stock bajo el mínimo de seguridad.
- **Asistente conversacional con IA**: interfaz de voz/texto que responde consultas sobre el negocio, operando exclusivamente sobre datos almacenados localmente.

## Equipo (Metodología Scrum)

| Integrante | Rol Scrum | Responsabilidad principal |
|---|---|---|
| Pablo Alvarado | Scrum Master | Adopción del marco ágil, organización de ceremonias, interfaz del Asistente Virtual |
| Gerard Perez | Product Owner | Gestión del backlog, priorización de historias, diseño visual del sistema |
| Domenica Crespo | Development Team | Integración de estadísticas y plantillas, pruebas del sistema |
| Karen Carabajo | Development Team | Lógica de negocio en Java, factura rápida, reportes PDF |
| Diana Tigre | Development Team | Base de datos relacional, modelado a objetos, componentes JavaFX |

## Alcance técnico

- **Lenguaje / Framework**: Java + JavaFX
- **Base de datos**: MySQL, operando localmente (sin datos sensibles en servidores externos)
- **Perfiles de usuario**: Administrador, Encargado de Inventario, Cajero/Facturador
- **Cumplimiento**: Facturación electrónica conforme a estándares SRI (XML firmado, Web Service SOAP)

## Organización del repositorio

El desarrollo se gestiona con Scrum sobre GitHub Issues, Milestones y Projects:

- **Milestones** = Sprints del cronograma (Sprint 1 a Sprint 6)
- **Issues** = Historias de usuario (`KRYP-XX`) y tareas técnicas
- **Labels** = tipo de issue, riesgo de desarrollo y módulo funcional
- **Project board** = tablero Kanban con campos de Estado, Prioridad y Story Points

Ver la pestaña [Projects](../../projects) para el tablero completo y [Milestones](../../milestones) para el detalle de cada Sprint.

## Institución

Instituto Superior Universitario Tecnológico del Azuay — Tecnología Superior en Desarrollo de Software.

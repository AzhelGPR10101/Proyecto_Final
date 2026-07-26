# 🔑 Krypton
### Gestión Inteligente de Inventario y Facturación para Microempresas de Cuenca

**Metodología:** SCRUM · **Estado:** En Desarrollo · **Instituto:** Tec. Azuay · **Stack:** Java / JavaFX

---

## 📌 Descripción del Proyecto

Software de escritorio que permite a las **microempresas y pequeños negocios de Cuenca** controlar su inventario, facturación y operación diaria sin depender de métodos manuales o plantillas rígidas, mediante una arquitectura de **plantillas maleables** que se adapta a cualquier giro de negocio, un **asistente conversacional con IA** local y un módulo de **auditoría proactiva (Smart-Auditing)**.

> *"Una arquitectura que se adapta al negocio, no el negocio a la arquitectura."*

---

## 🎯 Objetivo

Optimizar, simplificar y automatizar el control de stock y facturación de las microempresas de Cuenca mediante Programación Orientada a Objetos, reduciendo pérdidas por desabastecimiento, vencimiento de productos y toma de decisiones basada solo en intuición.

---

## 🏗️ Arquitectura del Sistema

```
├── App de Escritorio (JavaFX)  → Dashboard, plantillas, productos, facturación
├── Motor de Plantillas         → Campos y categorías dinámicas por negocio
├── Asistente Virtual (IA)      → Consultas por texto/voz sobre datos locales
├── Smart-Auditing              → Hilo en segundo plano (stock/vencimientos)
└── Base de Datos               → MySQL local (sin datos en servidores externos)
```

---

## 📂 Estructura del Repositorio

```
📁 Proyecto_Final/
├── 📄 README.md                    ← Este archivo
├── 📁 .github/
│   └── ISSUE_TEMPLATE/            ← Plantilla de historia de usuario
└── 📋 Issues, Milestones y Projects → Gestión completa del backlog Scrum
```

---

## 👥 Equipo SCRUM

| Rol              | Responsable        | Responsabilidad principal                                  |
|------------------|---------------------|--------------------------------------------------------------|
| Scrum Master     | Pablo Alvarado      | Adopción del marco ágil, ceremonias, Asistente Virtual        |
| Product Owner    | Gerard Perez        | Backlog, priorización de historias, diseño visual             |
| Dev Team         | Domenica Crespo     | Estadísticas, plantillas, pruebas del sistema                 |
| Dev Team         | Karen Carabajo      | Lógica de negocio Java, facturación rápida, reportes PDF      |
| Dev Team         | Diana Tigre         | Base de datos relacional, modelado a objetos, JavaFX          |

---

## 📋 Estado del Backlog

| Prioridad | Historias | Puntos |
|-----------|-----------|--------|
| 🔴 Alta   | 8         | 27 pts |
| 🟡 Media  | 6         | 22 pts |
| 🟢 Baja   | 2         | 5 pts  |
| **Total** | **16**    | **54** |

*Historias KRYP-01 a KRYP-16, cubren la totalidad de los requerimientos funcionales RF-01 a RF-15.*

---

## 🚀 Sprints Planificados

| Sprint   | Fechas            | Objetivo principal                                         |
|----------|--------------------|--------------------------------------------------------------|
| Sprint 1 | 29 jun - 12 jul    | Planificación, roles Scrum y requerimientos (RF/RNF)         |
| Sprint 2 | 13 jul - 26 jul    | Diseño de interfaces y mockups en JavaFX                    |
| Sprint 3 | 27 jul - 09 ago    | Codificación POO: motor de plantillas y CRUD                |
| Sprint 4 | 10 ago - 23 ago    | Base de datos MySQL y motor de facturación SRI (PDF)         |
| Sprint 5 | 24 ago - 30 ago    | Correo automático, Asistente IA y Smart-Auditing             |
| Sprint 6 | 31 ago - 04 sep    | Pruebas, corrección de errores y entrega final               |

---

## 🌍 Contexto

- **Ciudad:** Cuenca, Ecuador
- **Enfoque:** Microempresas y pequeños negocios locales
- **Cumplimiento legal:** Facturación electrónica conforme al SRI (XML firmado, Web Service SOAP)
- **Seguridad:** Datos operados 100% localmente, contraseñas cifradas con jBcrypt
- **Alineación:** Adaptabilidad comercial · Automatización de procesos · Toma de decisiones basada en datos

---

**Instituto Superior Universitario Tecnológico del Azuay** | Cuenca, Ecuador
**Carrera:** Tecnología Superior en Desarrollo de Software — Segundo M2A
**Docente:** Ing. Diana Romero

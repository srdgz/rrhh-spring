package gm.rrhh.controlador;

import gm.rrhh.excepcion.RecursoNoEncontradoExcepcion;
import gm.rrhh.modelo.Empleado;
import gm.rrhh.servicio.IEmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//http://localhost:8080/rrhh-app
@RequestMapping("rrhh-app")
//ruta para el frontend
@CrossOrigin(origins = "http://localhost:5173")

public class EmpleadoControlador {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);

    @Autowired
    private IEmpleadoServicio empleadoServicio;

    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados() {
        var empleados = empleadoServicio.listarEmpleados();
        return empleados;
    }

    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado) {
        return empleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Integer id) {
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if (empleado == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el empleado con id: " + id);
        } else {
            return ResponseEntity.ok(empleado);
        }
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleadoRecibido) {
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if (empleado == null) {
            throw new RecursoNoEncontradoExcepcion("El id de empleado recibido no existe: " + id);
        } else {
            empleado.setNombre(empleadoRecibido.getNombre());
            empleado.setApellido1(empleadoRecibido.getApellido1());
            empleado.setApellido2(empleadoRecibido.getApellido2());
            empleado.setDepartamento(empleadoRecibido.getDepartamento());
            empleado.setSueldo(empleadoRecibido.getSueldo());
            empleadoServicio.guardarEmpleado(empleado);
            return ResponseEntity.ok(empleado);
        }
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable Integer id) {
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if (empleado == null) {
            throw new RecursoNoEncontradoExcepcion("El id de empleado recibido no existe: " + id);
        } else {
            empleadoServicio.eliminarEmpleado(empleado);
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("Empleado eliminado", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);
        }
    }
}

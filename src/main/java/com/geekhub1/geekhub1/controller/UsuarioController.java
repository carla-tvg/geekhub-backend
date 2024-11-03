package com.geekhub1.geekhub1.controller;

import com.geekhub1.geekhub1.DTO.UsuarioDTO;
import com.geekhub1.geekhub1.model.Usuario;
import com.geekhub1.geekhub1.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuService;

    // Endpoint para obtener todas las personas
    @GetMapping("/traer")
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuService.obtenerUsuarios().stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioDTO> registrarUsuarios(@RequestBody Usuario usu) {
        Usuario nuevoUsuario = usuService.registrarUsuario(usu);
        return new ResponseEntity<>(convertirAUsuarioDTO(nuevoUsuario), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuService.eliminarUsuario(id);
        return new ResponseEntity<>("El usuario fue eliminado", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<UsuarioDTO> editarUsuario(@PathVariable Long id,
                                                    @RequestParam(required = false) String nuevoNombre,
                                                    @RequestParam(required = false) String nuevoApellido,
                                                    @RequestParam(required = false) String nuevoTelefono,
                                                    @RequestParam(required = false) String nuevoCorreo,
                                                    @RequestParam(required = false) String nuevoPassword) {
        usuService.editarUsuario(id, nuevoNombre, nuevoApellido, nuevoTelefono, nuevoCorreo, nuevoPassword);
        Usuario usu = usuService.obtenerUsuarioPorId(id);
        return new ResponseEntity<>(convertirAUsuarioDTO(usu), HttpStatus.OK);
    }

    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setTelefono(usuario.getTelefono());
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }
}

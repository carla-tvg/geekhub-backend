package com.geekhub1.geekhub1.controller;

import com.geekhub1.geekhub1.DTO.LoginDTO;
import com.geekhub1.geekhub1.DTO.UsuarioDTO;
import com.geekhub1.geekhub1.config.JwtUtil;
import com.geekhub1.geekhub1.model.Usuario;
import com.geekhub1.geekhub1.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto) {
        Usuario usuario = usuService.obtenerUsuarioPorCorreo(loginDto.getCorreo());
        if (usuario != null && passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) {
            // Pasar el rol como un Rol (no como String)
            String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    // Endpoint para obtener la información del usuario por su ID
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerPerfil(@RequestHeader("Authorization") String token) {
        // Extraer el correo del JWT (verifica que la implementación de JwtUtil esté configurada correctamente)
        String correo = jwtUtil.extractUsername(token.substring(7)); // Asumimos que el token está en el formato "Bearer <token>"

        Usuario usuario = usuService.obtenerUsuarioPorCorreo(correo); // Obtén el usuario por correo
        if (usuario != null) {
            return ResponseEntity.ok(convertirAUsuarioDTO(usuario)); // Devuelve la información del usuario
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}

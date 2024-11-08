package com.geekhub1.geekhub1.service;

import com.geekhub1.geekhub1.model.Rol;
import com.geekhub1.geekhub1.model.Usuario;
import com.geekhub1.geekhub1.repository.IUsuarioRepository;
import com.geekhub1.geekhub1.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

    @Autowired
    @Lazy
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario registrarUsuario(Usuario usu) {
        // Establecer rol predeterminado si no se ha asignado uno
        if (usu.getRol() == null) {
            usu.setRol(Rol.USER);
        }
        // Cifrar la contraseña antes de guardarla
        usu.setPassword(passwordEncoder.encode(usu.getPassword()));
        return usuarioRepository.save(usu);
    }

    // Método para autenticar y generar el token JWT con rol
    public String iniciarSesion(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            // Pasar el rol al generar el token
            return jwtUtil.generateToken(correo, usuario.getRol());
        }
        return null;  // Si la autenticación falla, devolver null
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void editarUsuario(Long idOriginal, String nuevoNombre, String nuevoApellido, String nuevoTelefono, String nuevoCorreo, String nuevoPassword) {
        Usuario usu = this.obtenerUsuarioPorId(idOriginal);

        if (usu != null) {
            if (nuevoNombre != null) usu.setNombre(nuevoNombre);
            if (nuevoApellido != null) usu.setApellido(nuevoApellido);
            if (nuevoTelefono != null) usu.setTelefono(nuevoTelefono);
            if (nuevoCorreo != null) usu.setCorreo(nuevoCorreo);
            if (nuevoPassword != null) {
                usu.setPassword(passwordEncoder.encode(nuevoPassword));
            }

            usuarioRepository.save(usu);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    // Implementación de UserDetailsService para cargar el usuario en base al correo
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }

        // Devolver el usuario con su rol para la autenticación
        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(usuario.getRol().name())  // Usar el nombre del rol como authority
                .build();
    }
}

package com.geekhub1.geekhub1.service;

import com.geekhub1.geekhub1.model.Usuario;
import com.geekhub1.geekhub1.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario registrarUsuario(Usuario usu) {
        return usuarioRepository.save(usu);
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
            if (nuevoPassword != null) usu.setPassword(nuevoPassword);

            usuarioRepository.save(usu);
        }
    }
}

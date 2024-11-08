package com.geekhub1.geekhub1.service;

import com.geekhub1.geekhub1.model.Usuario;
import java.util.List;

public interface IUsuarioService {

    public List<Usuario> obtenerUsuarios();
    public Usuario registrarUsuario(Usuario usu);
    public void eliminarUsuario(Long id);
    public Usuario obtenerUsuarioPorId(Long id);
    public void editarUsuario(Long idOriginal, String nuevoNombre, String nuevoApellido, String nuevoTelefono, String nuevoCorreo, String nuevoPassword);

    Usuario obtenerUsuarioPorCorreo(String correo);
}

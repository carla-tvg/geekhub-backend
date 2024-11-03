package com.geekhub1.geekhub1.repository;

import com.geekhub1.geekhub1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

}

package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Integer> {
}

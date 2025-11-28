package com.example.demo.application.service;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.interfaces.external.PromotorPortService;
import com.example.demo.application.services.ListarPromotorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListarPromotorServiceImplTest {

    @Mock private PromotorPortService port;
    @InjectMocks private ListarPromotorServiceImpl service;

    @Test
    void listar_DelegaCorrectamente() {
        PaginationResponseDTO<PromotorDashBoardDto> mockRes = new PaginationResponseDTO<>(
                List.of(), 1, 10, 0, 0, true
        );
        when(port.promotorListar("ADMIN", 1, 10)).thenReturn(mockRes);

        var result = service.listarPromotor("ADMIN", 1, 10);

        assertEquals(mockRes, result);
        verify(port).promotorListar("ADMIN", 1, 10);
    }
}

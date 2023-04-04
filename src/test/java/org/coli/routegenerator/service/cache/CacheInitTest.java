package org.coli.routegenerator.service.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.coli.routegenerator.constant.Constant.RUN_ZONE_CHASTRE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class CacheInitTest {

    @InjectMocks
    private CacheInit cacheInit;

    @Mock
    private CacheService cacheServiceMock;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loadCacheChastre() {
        doNothing().when(cacheServiceMock).findRoutesAndFillCache(anyInt(), eq(RUN_ZONE_CHASTRE));
        cacheInit.loadCacheChastre(10, 15);
        verify(cacheServiceMock, times(6)).findRoutesAndFillCache(anyInt(), eq(RUN_ZONE_CHASTRE));
    }
}

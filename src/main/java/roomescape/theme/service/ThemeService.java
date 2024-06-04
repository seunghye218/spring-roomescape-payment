package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.dto.ThemeAddRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    private static final int POPULAR_THEMES_LOOK_BACK_DAYS = 7;
    private static final int POPULAR_THEMES_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAllTheme() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findPopularTheme() {
        LocalDate periodEnd = LocalDate.now();
        LocalDate periodStart = periodEnd.minusDays(POPULAR_THEMES_LOOK_BACK_DAYS);

        Pageable pageRequest = PageRequest.of(0, POPULAR_THEMES_LIMIT_COUNT);
        return themeRepository.findTopByDurationAndCount(periodStart, periodEnd, pageRequest)
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse saveTheme(ThemeAddRequest themeAddRequest) {
        Theme saved = themeRepository.save(themeAddRequest.toTheme());
        return new ThemeResponse(saved);
    }

    public void removeTheme(Long id) {
        themeRepository.deleteById(id);
    }
}

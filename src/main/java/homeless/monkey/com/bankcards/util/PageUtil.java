package homeless.monkey.com.bankcards.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PageUtil {

    public static Pageable createPageable(int page, int size, String sort)
    {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        return PageRequest.of(page, size, sortObj);
    }
}

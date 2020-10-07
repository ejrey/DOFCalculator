package cmpt276.as2.model;

/**
 * Converted LensManager class into a Singleton model (Followed Videos provided)
 */

import java.util.ArrayList;
import java.util.List;

public class LensManager {
    private List<Lens> ls = new ArrayList<>();

    public void add(Lens lens) {
        ls.add(lens);
    }

    private static LensManager instance;
    private LensManager() {

    }

    public static LensManager getInstance() {
        if(instance == null) {
            instance = new LensManager();
        }
        return instance;
    }

    public Lens get(int i) {
        return ls.get(i);
    }

    public int listSize() {
        return ls.size();
    }
}

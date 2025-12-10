package org.example.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple line-based 3-way merger.
 * If both sides changed the same lines differently, produce conflict markers.
 * This is intentionally simple and robust.
 */
public class ThreeWayMerger {

    /**
     * Merge three texts (base, ours, theirs).
     * Returns a MergeResult object with mergedText and boolean conflict flag.
     */
    public static MergeResult mergeText(String base, String ours, String theirs) {
        // Null -> empty
        String b = base == null ? "" : base;
        String o = ours == null ? "" : ours;
        String t = theirs == null ? "" : theirs;

        if (o.equals(t)) {
            return new MergeResult(o, false);
        }
        if (b.equals(o) && !b.equals(t)) {
            return new MergeResult(t, false);
        }
        if (b.equals(t) && !b.equals(o)) {
            return new MergeResult(o, false);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<<<<<<< ours").append("\n");
        sb.append(o).append("\n");
        sb.append("=======").append("\n");
        sb.append(t).append("\n");
        sb.append(">>>>>>> theirs").append("\n");

        return new MergeResult(sb.toString(), true);
    }

    public static class MergeResult {
        public final String text;
        public final boolean conflict;
        public MergeResult(String text, boolean conflict) {
            this.text = text;
            this.conflict = conflict;
        }
    }
}

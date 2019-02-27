package parallelisierung;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultReciever {

    private FileWriter writer;
    private boolean test = false;
    private List<ResultEntry> entrylist;

    public ResultReciever(FileWriter writer) {
        if (writer != null) {
            this.writer = writer;
        } else {
            test = true;
            entrylist = new ArrayList<>();
        }
    }

    public void recieve(ResultEntry entry) {
        if (!test) {
            try {
                writer.write(entry.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            entrylist.add(entry);
        }
    }

    @Deprecated //For testing
    public List<ResultEntry> getEntrylist() {
        return entrylist;
    }
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

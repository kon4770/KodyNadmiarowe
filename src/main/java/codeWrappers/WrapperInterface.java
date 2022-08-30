package codeWrappers;

import java.util.List;
import java.util.Set;

public interface WrapperInterface {

    public boolean hasChanged();

    public void encode();

    public void decode(StringBuilder[] bitArray);

    public Set<Integer> getChunkToResendSet();
}

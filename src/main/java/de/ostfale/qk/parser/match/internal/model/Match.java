package de.ostfale.qk.parser.match.internal.model;

import de.ostfale.qk.parser.set.SetRawModel;

import java.util.List;

public interface Match {

    String getFirstPlayerOrTeamName();

    String getSecondPlayerOrTeamName();

    String getRoundName();

    List<SetRawModel> getPlayersSets();
}

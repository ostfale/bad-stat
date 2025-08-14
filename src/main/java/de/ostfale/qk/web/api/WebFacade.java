package de.ostfale.qk.web.api;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface WebFacade {

    String BASE_DBV_URL = "https://dbv.turnier.de/";
    String TOURNAMENT_DOWNLOAD_URL_PREFIX = "https://turniere.badminton.de/download?name=&year=";
    String TOURNAMENT_DOWNLOAD_SEARCH_PARAM = "&remaining=all&colortype=&jws=0&form%5Bsearch%5D=1&federation%5B%5D=70&federation%5B%5D" +
            "=89&federation%5B%5D=90&federation%5B%5D=91&federation%5B%5D=92&federation%5B%5D=71&federation%5B%5D=72&federation%5B%5D" +
            "=77&federation%5B%5D=79&federation%5B%5D=80&federation%5B%5D=81&federation%5B%5D=78&federation%5B%5D=74&federation%5B%5D" +
            "=85&federation%5B%5D=76&federation%5B%5D=73&federation%5B%5D=83&federation%5B%5D=82&federation%5B%5D=84&federation%5B%5D" +
            "=86&federation%5B%5D=87&federation%5B%5D=75&federation%5B%5D=88&agegroup%5B%5D=U9&agegroup%5B%5D=U11&agegroup%5B%5D=U13&agegroup%5B%5D" +
            "=U15&agegroup%5B%5D=U17&agegroup%5B%5D=U19&agegroup%5B%5D=U22&agegroup%5B%5D=O19&agegroup%5B%5D=O35&category%5B%5D=79&category%5B%5D" +
            "=80&category%5B%5D=81&category%5B%5D=82&category%5B%5D=83&category%5B%5D=107&category%5B%5D=106&category%5B%5D=104&category%5B%5D" +
            "=97&category%5B%5D=102&category%5B%5D=92&category%5B%5D=98&category%5B%5D=93&category%5B%5D=87&category%5B%5D=95&category%5B%5D" +
            "=91&category%5B%5D=88&category%5B%5D=103&category%5B%5D=94&category%5B%5D=84&category%5B%5D=86&category%5B%5D=100&category%5B%5D" +
            "=89&category%5B%5D=85&discipline%5B%5D=Einzel&discipline%5B%5D=Doppel&discipline%5B%5D=Mixed";

    String prepareDownloadUrl(String year);
    String prepareDownloadFileName(String year);
    String prepareDownloadTargetPath(String appDirName);

    CompletableFuture<Path> download(String url, Path target);


}

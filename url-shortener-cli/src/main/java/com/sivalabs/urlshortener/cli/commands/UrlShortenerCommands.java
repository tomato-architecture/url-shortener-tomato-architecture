package com.sivalabs.urlshortener.cli.commands;

import com.sivalabs.urlshortener.CoreProperties;
import com.sivalabs.urlshortener.domain.models.CreateShortUrlCmd;
import com.sivalabs.urlshortener.domain.models.PagedResult;
import com.sivalabs.urlshortener.domain.models.ShortUrlDto;
import com.sivalabs.urlshortener.domain.services.ShortUrlService;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command
public class UrlShortenerCommands {
    private final ShortUrlService shortUrlService;
    private final CoreProperties properties;

    public UrlShortenerCommands(ShortUrlService shortUrlService, CoreProperties properties) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
    }

    @Command(command = "list")
    public void listUrls(@Option(longNames = "page", shortNames = 'p', defaultValue = "1") int pageNo) {
        PagedResult<ShortUrlDto> shortUrls = shortUrlService.findPublicShortUrls(pageNo, properties.pageSize());
        System.out.println(format(shortUrls));
    }

    @Command(command = "create")
    public void create(@Option(longNames = "url", shortNames = 'u', required = true) String url) {
        CreateShortUrlCmd cmd = new CreateShortUrlCmd(url, false, null, null);
        ShortUrlDto shortUrl = shortUrlService.createShortUrl(cmd);
        System.out.println(format(shortUrl));
    }

    private String format(ShortUrlDto shortUrl) {
        return "\nShort URL:\n" + "------------------------------------------------------------\n"
                + String.format("%-10s %-15s %-40s%n", "ID", "Short Key", "Long URL")
                + "------------------------------------------------------------\n"
                + String.format("%-10d %-15s %-40s%n", shortUrl.id(), shortUrl.shortKey(), shortUrl.originalUrl())
                + "------------------------------------------------------------\n";
    }

    private String format(PagedResult<ShortUrlDto> shortUrls) {
        StringBuilder output = new StringBuilder();
        output.append("\nShort URLs List:\n");
        output.append("------------------------------------------------------------\n");
        output.append(String.format("%-10s %-15s %-40s%n", "ID", "Short Key", "Long URL"));
        output.append("------------------------------------------------------------\n");

        for (ShortUrlDto url : shortUrls.data()) {
            output.append(String.format("%-10d %-15s %-40s%n", url.id(), url.shortKey(), url.originalUrl()));
        }

        output.append("------------------------------------------------------------\n");
        output.append("Total URLs: ").append(shortUrls.totalElements()).append("\n");
        output.append("Current Page: ").append(shortUrls.pageNumber()).append("\n");
        output.append("Total Pages: ").append(shortUrls.totalPages()).append("\n");
        output.append("Has Next: ").append(shortUrls.hasNext()).append("\n");
        output.append("Has Previous: ").append(shortUrls.hasPrevious());
        return output.toString();
    }
}

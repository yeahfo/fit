package io.github.yeahfo.fit.common.email;


import io.github.yeahfo.fit.core.common.domain.UploadedFile;

import java.util.List;

public interface EmailSender {
    void sendVerificationCode( String email, String code );

    void notifyWebhookNotAccessible( List< String > emails, String appName, String webhookUrl );

    void sendInvoice( String email, List< UploadedFile > invoiceFiles );
}

package com.gkatzioura.maven.cloud.s3;

import static com.amazonaws.services.s3.model.CannedAccessControlList.AuthenticatedRead;
import static com.amazonaws.services.s3.model.CannedAccessControlList.AwsExecRead;
import static com.amazonaws.services.s3.model.CannedAccessControlList.BucketOwnerFullControl;
import static com.amazonaws.services.s3.model.CannedAccessControlList.BucketOwnerRead;
import static com.amazonaws.services.s3.model.CannedAccessControlList.Private;
import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class CannedAccessControlListPropertyTest {

    private static final PublicReadProperty PUBLIC_READ_TRUE = new PublicReadProperty(true);

    @Test
    public void defaultConstructorReturnsDefaultValue() {
        assertEquals(Private, new CannedAccessControlListProperty().get());
    }

    @Test
    public void cannedAclNameValue() {
        assertEquals(AuthenticatedRead, new CannedAccessControlListProperty("AuthenticatedRead", PUBLIC_READ_TRUE).get());
    }

    @Test
    public void cannedAclHeaderValue() {
        assertEquals(AwsExecRead, new CannedAccessControlListProperty("aws-exec-read", PUBLIC_READ_TRUE).get());
    }

    @Test
    public void nullCannedAclReturnsSystemPropertyValue() {
        System.setProperty("cannedAcl", "BucketOwnerRead");
        assertEquals(BucketOwnerRead, new CannedAccessControlListProperty().get());
    }

    @Ignore
    @Test
    public void nullCannedAclReturnsEnvironmentVariableValue() {
        // TODO: work out how to test environment variables for test
        //        System.setenv("CANNED_ACL", "BucketOwnerFullControl");
        assertEquals(BucketOwnerFullControl, new CannedAccessControlListProperty().get());
    }

    @Test
    public void nullCannedAclFallsBackToPublicReadProperty() {
        assertEquals(PublicRead, new CannedAccessControlListProperty(null, PUBLIC_READ_TRUE).get());
    }

    @Test
    public void nonsenseValueSetsToDefault() {
        assertEquals(Private, new CannedAccessControlListProperty("nonsense", PUBLIC_READ_TRUE).get());
    }
}

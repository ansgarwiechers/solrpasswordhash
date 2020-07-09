/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. The ASF licenses this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.  You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Adopted from the Solr Sha256AuthenticationProvider class.
 * (solr/core/src/java/org/apache/solr/security/Sha256AuthenticationProvider.java)
 *
 * URL: <https://github.com/apache/lucene-solr>
 */
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;

public class SolrPasswordHash {
  public static void main (String[] args) {
    if (args.length == 0) {
      System.err.println("Nothing to do.");
      System.exit(1);
    }

    String pw = args[0];
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");

      final Random r = new SecureRandom();
      byte[] salt = new byte[32];
      r.nextBytes(salt);

      digest.reset();
      digest.update(salt);
      byte[] btPass = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
      digest.reset();
      btPass = digest.digest(btPass);

      System.out.println(Base64.encodeBase64String(btPass) + " " +
        Base64.encodeBase64String(salt));
      System.exit(0);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Unknown algorithm: " + e.getMessage());
    }
    System.exit(2);
  }
}

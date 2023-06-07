package hcmute.edu.vn.chathtp.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAvatar {
    private List<String> listAvatar;
    private Random random;

    public RandomAvatar() {
        listAvatar = new ArrayList<>();
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default2.jpg?alt=media&token=2d499114-64ae-4a41-980c-8ab3c7a88d12");
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default3.jpg?alt=media&token=9653a5b2-ec0d-4b82-8923-3c5d50688b72");
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default4.jpg?alt=media&token=ac26f8f8-33d7-4a39-84de-40f1f18b1ede");
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default5.jpg?alt=media&token=ce15c5a0-4ad9-4bc2-8a27-8177a42b6fe1");
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default6.jpg?alt=media&token=9fab5353-11e5-465f-b24f-39b9c65da6d9");
        listAvatar.add("https://firebasestorage.googleapis.com/v0/b/cnpm05-8d4af.appspot.com/o/default7.jpg?alt=media&token=f84f3539-f6da-48df-ad25-00354f1e1064");
        random = new Random();
    }
    public String getRandomAvatar() {
        int randomIndex = random.nextInt(listAvatar.size());
        return listAvatar.get(randomIndex);
    }
}

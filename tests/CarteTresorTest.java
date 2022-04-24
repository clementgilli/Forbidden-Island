import junit.framework.TestCase;
import models.CarteTresor;
import models.Plateau;
import models.Player;

public class CarteTresorTest extends TestCase {
    public void test() throws Exception {
        CarteTresor carte = new CarteTresor(CarteTresor.TYPE_CARTE_TRESOR.ARTEF_FEU);
        assertEquals(carte.getValeurCarte(), CarteTresor.TYPE_CARTE_TRESOR.ARTEF_FEU);
        assertEquals(CarteTresor.getLastNewId(), 0);
        CarteTresor carteBis = new CarteTresor(CarteTresor.TYPE_CARTE_TRESOR.CARTE_DE_SABLE);
        assertEquals(carte.getValeurCarte(), CarteTresor.TYPE_CARTE_TRESOR.CARTE_DE_SABLE);
        assertEquals(CarteTresor.getLastNewId(), 1);
    }
}

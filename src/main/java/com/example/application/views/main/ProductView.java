package com.example.application.views.main;

import com.example.application.models.Category;
import com.example.application.models.Message;
import com.example.application.models.Product;
import com.example.application.models.User;
import com.example.application.services.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import lombok.var;
import org.apache.commons.compress.utils.IOUtils;

import javax.validation.constraints.Null;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Route
public class ProductView extends VerticalLayout {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final MessageService messageService;

    Long itemIdForEdition = 0L;

    Grid<Product> grid = new Grid<>(Product.class);
    Dialog messageDialog = new Dialog();
    Dialog dialog = new Dialog();

    public ProductView(ProductService productService, CategoryService categoryService, UserService userService, MessageService messageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.messageService = messageService;

        dialog.setModal(true);
        //messageDialog.setModal(true);
        messageDialog.setWidth("300px");
        messageDialog.setHeight("300px");

        //Properties
        //Date Begin
        DatePicker valueDatePicker = new DatePicker();
        valueDatePicker.setLabel("Tarih");
        LocalDate now = LocalDate.now();
        valueDatePicker.setValue(now);
        //Date Last


        //-----------------------------------------------------------------------------------------------------------------
        //image begin
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();
        Image image = new Image();
        image.setWidth("300px");
        image.setHeight("300px");


        upload.setReceiver(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            try {

                byte[] imageBytes = IOUtils.toByteArray(buffer.getInputStream(event.getFileName()));
                StreamResource resource = new StreamResource(event.getFileName(), () -> new ByteArrayInputStream(imageBytes));
                image.setSrc(resource);
                image.setTitle(event.getFileName());
                output.removeAll();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        upload.getElement().addEventListener("file-remove", event -> {
            output.removeAll();
            image.setSrc("");
            image.setTitle("");
        });


        //image last
        //City Begin
        ComboBox selectCity = new ComboBox<>("??ehir");
        String[] cities = new String[]{"Adana", "Adiyaman", "Afyon", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydin",
                "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa", "Canakkale", "Cankiri",
                "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir", "Gaziantep", "Giresun",
                "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul", "Izmir", "Kahramanmaras", "Karabuk", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa",
                "Mardin", "Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt",
                "Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"};

        selectCity.setItems(cities);
        selectCity.setPlaceholder("??l Se??iniz");
        //City Last
        //-----------------------------------------------------------------------------------------------------------------
        //City District Begin
        ComboBox selectCityDistrict = new ComboBox<>("??l??e");
        selectCityDistrict.setPlaceholder("??l??e Se??iniz");

        String[] adana = new String[]{"Alada??", "Ceyhan", "??ukurova", "Feke", "??mamo??lu", "Karaisal??", "Karata??", "Kozan",
                "Pozant??", "Saimbeyli", "Sar????am", "Seyhan", "Tufanbeyli", "Yumurtal??k", "Y??re??ir"};

        String[] adiyaman = new String[]{"Ad??yaman (??l merkezi)", "Besni", "??elikhan", "Gerger", "G??lba????", "K??hta", "Samsat", "Sincik", "Tut"};
        String[] afyon = new String[]{"Afyonkarahisar (??l merkezi)", "Ba??mak????", "Bayat", "Bolvadin", "??ay", "??obanlar", "Dazk??r??", "Dinar", "Emirda??",
                "Evciler", "Hocalar", "??hsaniye", "??scehisar", "K??z??l??ren", "Sand??kl??", "Sinanpa??a", "Sultanda????", "??uhut"};
        String[] agri = new String[]{"A??r?? (il merkezi)", "Diyadin", "Do??ubayaz??t", "Ele??kirt", "Hamur", "Patnos", "Ta??l????ay", "Tutak"};
        String[] aksaray = new String[]{"Aksaray (??l merkezi)", "A??a????ren", "Eskil", "G??la??a??", "G??zelyurt", "Ortak??y", "Sar??yah??i", "Sultanhan??"};
        String[] amasya = new String[]{"Amasya (il merkezi)", "G??yn??cek", "G??m????hac??k??y", "Hamam??z??", "Merzifon", "Suluova", "Ta??ova"};
        String[] ankara = new String[]{"Akyurt", "Alt??nda??", "Aya??", "Bal??", "Beypazar??", "??aml??dere", "??ankaya", "??ubuk", "Elmada??", "Etimesgut",
                "Evren", "G??lba????", "G??d??l", "Haymana", "Kahramankazan", "Kalecik", "Ke??i??ren", "K??z??lcahamam", "Mamak",
                "Nall??han", "Polatl??", "Pursaklar", "Sincan", "??erefliko??hisar", "Yenimahalle"};
        String[] antalya = new String[]{"Akseki", "Aksu", "Alanya", "Demre", "D????emealt??", "Elmal??", "Finike", "Gazipa??a", "G??ndo??mu??", "??brad??",
                "Ka??", "Kemer", "Kepez", "Konyaalt??", "Korkuteli", "Kumluca", "Manavgat", "Muratpa??a", "Serik"};
        String[] ardahan = new String[]{"Ardahan (il merkezi)", "????ld??r", "Damal", "G??le", "Hanak", "Posof"};
        String[] artvin = new String[]{"Artvin (il merkezi)", "Ardanu??", "Arhavi", "Bor??ka", "Hopa", "Kemalpa??a", "Murgul", "??av??at", "Yusufeli"};
        String[] aydin = new String[]{"Bozdo??an", "Buharkent", "??ine", "Germencik", "??ncirliova", "Karacasu", "Ko??arl??", "K????k", "Ku??adas??", "Kuyucak",
                "Nazilli", "S??ke", "Sultanhisar", "Yenipazar"};
        String[] balikesir = new String[]{"Alt??eyl??l", "Ayval??k", "Balya", "Band??rma", "Bigadi??", "Burhaniye", "Dursunbey", "Edremit", "Erdek", "G??me??", "G??nen",
                "Havran", "??vrindi", "Karesi", "Kepsut", "Manyas", "Marmara", "Sava??tepe", "S??nd??rg??", "Susurluk"};
        String[] bartin = new String[]{"Bart??n (il merkezi)", "Amasra", "Kuruca??ile", "Ulus"};
        String[] batman = new String[]{"Batman (il merkezi)", "Be??iri", "Gerc????", "Hasankeyf", "Kozluk", "Sason"};
        String[] bayburt = new String[]{"Bayburt (il merkezi)", "Demir??ren ", "Ayd??ntepe "};
        String[] bilecik = new String[]{"Bilecik (il merkezi)", "Boz??y??k", "G??lpazar??", "??nhisar", "Osmaneli", "Pazaryeri", "S??????t", "Yenipazar"};
        String[] bing??l = new String[]{"Bing??l (il merkezi)", "Adakl??", "Gen??", "Karl??ova", "Ki????", "Solhan", "Yayladere", "Yedisu"};
        String[] bitlis = new String[]{"Bitlis (il merkezi)", "Adilcevaz", "Ahlat", "G??roymak", "Hizan", "Mutki", "Tatvan"};
        String[] bolu = new String[]{"Bolu (il merkezi)", "D??rtdivan", "Gerede", "G??yn??k", "K??br??sc??k", "Mengen", "Mudurnu", "Seben"};
        String[] burdur = new String[]{"Burdur (il merkezi)", "A??lasun", "Alt??nyayla", "Bucak", "??avd??r", "??eltik??i", "G??lhisar", "Karamanl??"};
        String[] bursa = new String[]{"B??y??korhan", "Gemlik", "G??rsu", "Harmanc??k", "??neg??l", "??znik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpa??a",
                "Nil??fer", "Orhaneli", "Orhangazi", "Osmangazi", "Yeni??ehir", "Y??ld??r??m"};
        String[] canakkale = new String[]{"??anakkale (il merkezi)", "Ayvac??k", "Bayrami??", "Biga", "Bozcaada", "??an", "Eceabat", "Ezine", "Gelibolu", "G??k??eada",
                "Lapseki", "Yenice"};
        String[] cankiri = new String[]{"??ank??r?? (il merkezi)", "Atkaracalar", "Bayram??ren", "??erke??", "Eldivan", "Ilgaz", "K??z??l??rmak", "Korgun", "Kur??unlu", "Orta",
                "??aban??z??", "Yaprakl??"};
        String[] corum = new String[]{"??orum (il merkezi)", "Alaca", "Bayat", "Bo??azkale", "Dodurga", "??skilip", "Karg??", "La??in", "Mecit??z??", "O??uzlar", "Ortak??y", "Osmanc??k",
                "Sungurlu", "U??urluda??"};
        String[] denizli = new String[]{"Ac??payam", "Babada??", "Baklan", "Bekilli", "Beya??a??", "Bozkurt", "Buldan", "??al", "??ameli", "??ardak", "??ivril", "G??ney", "Honaz", "Kale",
                "Merkezefendi", "Pamukkale", "Sarayk??y", "Serinhisar", "Tavas"};
        String[] diyarbakir = new String[]{"Ba??lar", "Bismil", "??ermik", "????nar", "????ng????", "Dicle", "E??il", "Ergani", "Hani", "Hazro", "Kayap??nar", "Kocak??y", "Kulp", "Lice", "Silvan",
                "Sur", "Yeni??ehir"};
        String[] d??zce = new String[]{"D??zce (il merkezi)", "Ak??akoca", "Cumayeri", "??ilimli", "G??lyaka", "G??m????ova", "Kayna??l??", "Y??????lca"};
        String[] edirne = new String[]{"Edirne (il merkezi)", "Enez", "Havsa", "??psala", "Ke??an", "Lalapa??a", "Meri??", "S??lo??lu", "Uzunk??pr??"};
        String[] elazig = new String[]{"El??z???? (il merkezi)", "A????n", "Alacakaya", "Ar??cak", "Baskil", "Karako??an", "Keban", "Kovanc??lar", "Maden", "Palu", "Sivrice"};
        String[] erzincan = new String[]{"Erzincan (il merkezi)", "??ay??rl??", "??li??", "Kemah", "Kemaliye", "Otlukbeli", "Refahiye", "Tercan", "??z??ml??"};
        String[] erzurum = new String[]{"A??kale", "Aziziye", "??at", "H??n??s", "Horasan", "??spir", "Kara??oban", "Karayaz??", "K??pr??k??y", "Narman", "Oltu", "Olur", "Paland??ken", "Pasinler",
                "Pazaryolu", "??enkaya", "Tekman", "Tortum", "Uzundere", "Yakutiye"};
        String[] eskisehir = new String[]{"Alpu", "Beylikova", "??ifteler", "G??ny??z??", "Han", "??n??n??", "Mahmudiye", "Mihalgazi", "Mihal????????k", "Odunpazar??", "Sar??cakaya", "Seyitgazi",
                "Sivrihisar", "Tepeba????"};
        String[] gaziantep = new String[]{"Araban", "??slahiye", "Karkam????", "Nizip", "Nurda????", "O??uzeli", "??ahinbey", "??ehitkamil", "Yavuzeli"};
        String[] giresun = new String[]{"Piraziz", "Bulancak", "Giresun (il merkezi)", "Ke??ap", "Espiye", "Tirebolu", "G??rele", "Eynesil"};
        String[] gumushane = new String[]{"G??m????hane (il merkezi)", "Kelkit", "K??se", "K??rt??n", "??iran", "Torul"};
        String[] hakkari = new String[]{"Hakk??ri (il merkezi)", "??ukurca", "??emdinli", "Y??ksekova", "Derecik "};
        String[] hatay = new String[]{"Alt??n??z??", "Antakya", "Arsuz", "Belen", "Defne", "D??rtyol", "Erzin", "Hassa", "??skenderun", "K??r??khan", "Kumlu", "Payas", "Reyhanl??",
                "Samanda??", "Yaylada????"};
        String[] igdir = new String[]{"I??d??r (il merkezi)", "Aral??k", "Karakoyunlu", "Tuzluca"};
        String[] isparta = new String[]{"Isparta (??l merkezi)", "Aksu", "Atabey", "E??irdir", "Gelendost", "G??nen", "Ke??iborlu", "Senirkent", "S??t????ler", "??arkikaraa??a??",
                "Uluborlu", "Yalva??", "Yeni??arbademli"};
        String[] istanbul = new String[]{"Adalar", "Arnavutk??y", "Ata??ehir", "Avc??lar", "Ba??c??lar", "Bah??elievler", "Bak??rk??y", "Ba??ak??ehir", "Bayrampa??a", "Be??ikta??",
                "Beykoz", "Beylikd??z??", "Beyo??lu", "B??y??k??ekmece", "??atalca", "??ekmek??y", "Esenler", "Esenyurt", "Ey??psultan", "Fatih", "Gaziosmanpa??a", "G??ng??ren",
                "Kad??k??y", "K??????thane", "Kartal", "K??????k??ekmece", "Maltepe", "Pendik", "Sancaktepe", "Sar??yer", "Silivri", "Sultanbeyli", "Sultangazi", "??ile", "??i??li",
                "Tuzla", "??mraniye", "??sk??dar", "Zeytinburnu"};
        String[] izmir = new String[]{"Alia??a", "Bal??ova", "Bay??nd??r", "Bayrakl??", "Bergama", "Beyda??", "Bornova", "Buca", "??e??me", "??i??li", "Dikili", "Fo??a", "Gaziemir",
                "G??zelbah??e", "Karaba??lar", "Karaburun", "Kar????yaka", "Kemalpa??a", "K??n??k", "Kiraz", "Konak", "Menderes", "Menemen", "Narl??dere", "??demi??", "Seferihisar",
                "Sel??uk", "Tire", "Torbal??", "Urla"};
        String[] kahramanmaras = new String[]{"Af??in", "And??r??n", "??a??layancerit", "Dulkadiro??lu", "Ekin??z??", "Elbistan", "G??ksun", "Nurhak", "Oniki??ubat", "Pazarc??k",
                "T??rko??lu"};
        String[] karabuk = new String[]{"Karab??k (??l merkezi)", "Eflani", "Eskipazar", "Ovac??k", "Safranbolu", "Yenice"};
        String[] karaman = new String[]{"Karaman (il merkezi)", "Ayranc??", "Ba??yayla", "Ermenek", "Kaz??mkarabekir", "Sar??veliler"};
        String[] kars = new String[]{"Kars (il merkezi)", "Akyaka", "Arpa??ay", "Digor", "Ka????zman", "Sar??kam????", "Selim", "Susuz"};
        String[] kastamonu = new String[]{"Merkez", "Abana", "A??l??", "Ara??", "Azdavay", "Bozkurt", "Cide", "??atalzeytin", "Daday", "Devrekani", "Do??anyurt", "Han??n??",
                "??hsangazi", "??nebolu", "K??re", "P??narba????", "Seydiler", "??enpazar", "Ta??k??pr??", "Tosya"};
        String[] kayseri = new String[]{"Akk????la", "B??nyan", "Develi", "Felahiye", "Hac??lar", "??ncesu", "Kocasinan", "Melikgazi", "??zvatan", "P??narba????", "Sar??o??lan",
                "Sar??z", "Talas", "Tomarza", "Yahyal??", "Ye??ilhisar"};
        String[] kilis = new String[]{"Kilis (??l merkezi)", "Elbeyli", "Musabeyli", "Polateli"};
        String[] kirikkale = new String[]{"K??r??kkale (il merkezi)", "Bah??ili", "Bal????eyh", "??elebi", "Delice", "Karake??ili", "Keskin", "Sulakyurt", "Yah??ihan"};
        String[] kirklareli = new String[]{"Babaeski", "Demirk??y", "Kof??az", "L??leburgaz", "Pehlivank??y", "P??narhisar", "Vize'"};
        String[] kirsehir = new String[]{"K??r??ehir (??l merkezi)", "Ak??akent", "Akp??nar", "Boztepe", "??i??ekda????", "Kaman", "Mucur"};
        String[] kocaeli = new String[]{"Ba??iskele", "??ay??rova", "Dar??ca", "Derince", "Dilovas??", "Gebze", "G??lc??k", "??zmit", "Kand??ra", "Karam??rsel", "Kartepe",
                "K??rfez"};
        String[] konya = new String[]{"Ah??rl??", "Ak??ren", "Ak??ehir", "Alt??nekin", "Bey??ehir", "Bozk??r", "Cihanbeyli", "??eltik", "??umra", "Derbent", "Derebucak",
                "Do??anhisar", "Emirgazi", "Ere??li", "G??neys??n??r", "Hadim", "Halkap??nar", "H??y??k", "Ilg??n", "Kad??nhan??", "Karap??nar", "Karatay", "Kulu", "Meram", "Saray??n??",
                "Sel??uklu", "Seydi??ehir", "Ta??kent", "Tuzluk??u", "Yal??h??y??k", "Yunak"};
        String[] kutahya = new String[]{"K??tahya (??l merkezi)", "Alt??nta??", "Aslanapa", "??avdarhisar", "Domani??", "Dumlup??nar", "Emet", "Gediz", "Hisarc??k", "Pazarlar",
                "??aphane", "Simav", "Tav??anl??"};
        String[] malatya = new String[]{"Ak??ada??", "Arapgir", "Arguvan", "Battalgazi", "Darende", "Do??an??ehir", "Do??anyol", "Hekimhan", "Kale", "Kuluncak", "P??t??rge",
                "Yaz??han", "Ye??ilyurt"};
        String[] manisa = new String[]{"Ahmetli", "Akhisar", "Ala??ehir", "Demirci", "G??lmarmara", "G??rdes", "K??rka??a??", "K??pr??ba????", "Kula", "Salihli", "Sar??g??l",
                "Saruhanl??", "Selendi", "Soma", "??ehzadeler", "Turgutlu", "Yunusemre"};
        String[] mardin = new String[]{"Artuklu", "Darge??it", "Derik", "K??z??ltepe", "Maz??da????", "Midyat", "Nusaybin", "??merli", "Savur", "Ye??illi"};
        String[] mersin = new String[]{"Akdeniz", "Anamur", "Ayd??nc??k", "Bozyaz??", "??aml??yayla", "Erdemli", "G??lnar", "Mezitli", "Mut", "Silifke", "Tarsus", "Toroslar",
                "Yeni??ehir"};
        String[] mugla = new String[]{"Bodrum", "Dalaman", "Dat??a", "Fethiye", "Kavakl??dere", "K??yce??iz", "Marmaris", "Mente??e", "Milas", "Ortaca", "Seydikemer", "Ula",
                "Yata??an"};
        String[] mus = new String[]{"Mu?? (??l merkezi)", "Bulan??k", "Hask??y", "Korkut", "Malazgirt", "Varto"};
        String[] nevsehir = new String[]{"Nev??ehir (merkez il??e)", "Ac??g??l", "Avanos", "Derinkuyu", "G??l??ehir", "Hac??bekta??", "Kozakl??", "??rg??p"};
        String[] nigde = new String[]{"Ni??de (??l merkezi)", "Altunhisar", "Bor", "??amard??", "??iftlik", "Uluk????la"};
        String[] ordu = new String[]{"Akku??", "Alt??nordu", "Aybast??", "??ama??", "??atalp??nar", "??ayba????", "Fatsa", "G??lk??y", "G??lyal??", "??kizce", "Kabad??z", "Kabata??",
                "Korgan", "Kumru", "Mesudiye", "Per??embe", "Ulubey", "??nye"};
        String[] osmaniye = new String[]{"Osmaniye (il merkezi)", "Bah??e", "D??zi??i", "Hasanbeyli", "Kadirli", "Sumbas", "Toprakkale"};
        String[] rize = new String[]{"Rize (il merkezi)", "Arde??en", "??aml??hem??in", "??ayeli", "Derepazar??", "F??nd??kl??", "G??neysu", "Hem??in", "??kizdere", "??yidere",
                "Kalkandere", "Pazar"};
        String[] sakarya = new String[]{"Adapazar??", "Akyaz??", "Arifiye", "Erenler", "Ferizli", "Geyve", "Hendek", "Karap??r??ek", "Karasu", "Kaynarca", "Kocaali",
                "Pamukova", "Sapanca", "Serdivan", "S??????tl??", "Tarakl??"};
        String[] samsun = new String[]{"19 May??s", "Ala??am", "Asarc??k", "Atakum", "Ayvac??k", "Bafra", "Canik", "??ar??amba", "Havza", "??lkad??m", "Kavak", "Ladik",
                "Sal??pazar??", "Tekkek??y", "Terme", "Vezirk??pr??", "Yakakent"};
        String[] sanliurfa = new String[]{"Ak??akale", "Birecik", "Bozova", "Ceylanp??nar", "Halfeti", "Harran", "Hilvan", "Siverek", "Suru??", "Viran??ehir", "Karak??pr??",
                "Haliliye", "Eyy??biye"};
        String[] siirt = new String[]{"Siirt (??l merkezi)", "Baykan", "Eruh", "Kurtalan", "Pervari", "??irvan", "Tillo"};
        String[] sinop = new String[]{"Sinop (il merkezi)", "Ayanc??k", "Boyabat", "Dikmen", "Dura??an", "Erfelek", "Gerze", "Sarayd??z??", "T??rkeli"};
        String[] sirnak = new String[]{"????rnak (il merkezi)", "Beyt??????ebap", "Cizre", "G????l??konak", "??dil", "Silopi", "Uludere"};
        String[] sivas = new String[]{"Sivas (??l merkezi)", "Ak??nc??lar", "Alt??nyayla", "Divri??i", "Do??an??ar", "Gemerek", "G??lova", "G??r??n", "Hafik", "??mranl??", "Kangal",
                "Koyulhisar", "Su??ehri", "??ark????la", "Ula??", "Y??ld??zeli", "Zara"};
        String[] tekirdag = new String[]{"S??leymanpa??a", "??erkezk??y", "??orlu", "Ergene", "Hayrabolu", "Kapakl??", "Malkara", "Marmaraere??lisi", "Muratl??", "Saray",
                "??ark??y"};
        String[] tokat = new String[]{"Tokat (il merkezi)", "Almus", "Artova", "Ba????iftlik", "Erbaa", "Niksar", "Pazar", "Re??adiye", "Sulusaray", "Turhal", "Ye??ilyurt",
                "Zile"};
        String[] trabzon = new String[]{"Ak??aabat", "Arakl??", "Arsin", "Be??ikd??z??", "??ar????ba????", "??aykara", "Dernekpazar??", "D??zk??y", "Hayrat", "K??pr??ba????", "Ma??ka",
                "Of", "Ortahisar", "S??rmene", "??alpazar??", "Tonya", "Vakf??kebir", "Yomra"};
        String[] tunceli = new String[]{"Tunceli (il merkezi)", "??emi??gezek", "Hozat", "Mazgirt", "Naz??miye", "Ovac??k", "Pertek", "P??l??m??r"};
        String[] usak = new String[]{"U??ak (il merkezi)", "Banaz", "E??me", "Karahall??", "Sivasl??", "Ulubey"};
        String[] van = new String[]{"Bah??esaray", "Ba??kale", "??ald??ran", "??atak", "Edremit", "Erci??", "Geva??", "G??rp??nar", "??pekyolu", "Muradiye", "??zalp", "Saray",
                "Tu??ba"};
        String[] yalova = new String[]{"Yalova (il merkezi)", "Alt??nova", "Armutlu", "????narc??k", "??iftlikk??y", "Termal"};
        String[] yozgat = new String[]{"Yozgat (il merkezi)", "Akda??madeni", "Ayd??nc??k", "Bo??azl??yan", "??and??r", "??ay??ralan", "??ekerek", "Kad????ehri", "Saraykent",
                "Sar??kaya", "Sorgun", "??efaatli", "Yenifak??l??", "Yerk??y"};
        String[] zonguldak = new String[]{"Zonguldak (il merkezi)", "Alapl??", "??aycuma", "Devrek", "G??k??ebey", "Karadeniz Ere??li", "Kilimli", "Kozlu"};

        selectCity.addValueChangeListener(event -> {
            if (event.getValue().equals("Adana")) selectCityDistrict.setItems(adana);
            else if (event.getValue().equals("Adiyaman")) selectCityDistrict.setItems(adiyaman);
            else if (event.getValue().equals("Afyon")) selectCityDistrict.setItems(afyon);
            else if (event.getValue().equals("Agri")) selectCityDistrict.setItems(agri);
            else if (event.getValue().equals("Aksaray")) selectCityDistrict.setItems(aksaray);
            else if (event.getValue().equals("Amasya")) selectCityDistrict.setItems(amasya);
            else if (event.getValue().equals("Ankara")) selectCityDistrict.setItems(ankara);
            else if (event.getValue().equals("Antalya")) selectCityDistrict.setItems(antalya);
            else if (event.getValue().equals("Ardahan")) selectCityDistrict.setItems(ardahan);
            else if (event.getValue().equals("Artvin")) selectCityDistrict.setItems(artvin);
            else if (event.getValue().equals("Aydin")) selectCityDistrict.setItems(aydin);
            else if (event.getValue().equals("Balikesir")) selectCityDistrict.setItems(balikesir);
            else if (event.getValue().equals("Bartin")) selectCityDistrict.setItems(bartin);
            else if (event.getValue().equals("Batman")) selectCityDistrict.setItems(batman);
            else if (event.getValue().equals("Bayburt")) selectCityDistrict.setItems(bayburt);
            else if (event.getValue().equals("Bilecik")) selectCityDistrict.setItems(bilecik);
            else if (event.getValue().equals("Bingol")) selectCityDistrict.setItems(bing??l);
            else if (event.getValue().equals("Bitlis")) selectCityDistrict.setItems(bitlis);
            else if (event.getValue().equals("Bolu")) selectCityDistrict.setItems(bolu);
            else if (event.getValue().equals("Burdur")) selectCityDistrict.setItems(burdur);
            else if (event.getValue().equals("Bursa")) selectCityDistrict.setItems(bursa);
            else if (event.getValue().equals("Canakkale")) selectCityDistrict.setItems(canakkale);
            else if (event.getValue().equals("Cankiri")) selectCityDistrict.setItems(cankiri);
            else if (event.getValue().equals("Corum")) selectCityDistrict.setItems(corum);
            else if (event.getValue().equals("Denizli")) selectCityDistrict.setItems(denizli);
            else if (event.getValue().equals("Diyarbakir")) selectCityDistrict.setItems(diyarbakir);
            else if (event.getValue().equals("Duzce")) selectCityDistrict.setItems(d??zce);
            else if (event.getValue().equals("Edirne")) selectCityDistrict.setItems(edirne);
            else if (event.getValue().equals("Elazig")) selectCityDistrict.setItems(elazig);
            else if (event.getValue().equals("Erzincan")) selectCityDistrict.setItems(erzincan);
            else if (event.getValue().equals("Erzurum")) selectCityDistrict.setItems(erzurum);
            else if (event.getValue().equals("Eskisehir")) selectCityDistrict.setItems(eskisehir);
            else if (event.getValue().equals("Gaziantep")) selectCityDistrict.setItems(gaziantep);
            else if (event.getValue().equals("Giresun")) selectCityDistrict.setItems(giresun);
            else if (event.getValue().equals("Gumushane")) selectCityDistrict.setItems(gumushane);
            else if (event.getValue().equals("Hakkari")) selectCityDistrict.setItems(hakkari);
            else if (event.getValue().equals("Hatay")) selectCityDistrict.setItems(hatay);
            else if (event.getValue().equals("Igdir")) selectCityDistrict.setItems(igdir);
            else if (event.getValue().equals("Isparta")) selectCityDistrict.setItems(isparta);
            else if (event.getValue().equals("Istanbul")) selectCityDistrict.setItems(istanbul);
            else if (event.getValue().equals("Izmir")) selectCityDistrict.setItems(izmir);
            else if (event.getValue().equals("Kahramanmaras")) selectCityDistrict.setItems(kahramanmaras);
            else if (event.getValue().equals("Karabuk")) selectCityDistrict.setItems(karabuk);
            else if (event.getValue().equals("Karaman")) selectCityDistrict.setItems(karaman);
            else if (event.getValue().equals("Kars")) selectCityDistrict.setItems(kars);
            else if (event.getValue().equals("Kastamonu")) selectCityDistrict.setItems(kastamonu);
            else if (event.getValue().equals("Kayseri")) selectCityDistrict.setItems(kayseri);
            else if (event.getValue().equals("Kilis")) selectCityDistrict.setItems(kilis);
            else if (event.getValue().equals("Kirikkale")) selectCityDistrict.setItems(kirikkale);
            else if (event.getValue().equals("Kirklareli")) selectCityDistrict.setItems(kirklareli);
            else if (event.getValue().equals("Kirsehir")) selectCityDistrict.setItems(kirsehir);
            else if (event.getValue().equals("Kocaeli")) selectCityDistrict.setItems(kocaeli);
            else if (event.getValue().equals("Konya")) selectCityDistrict.setItems(konya);
            else if (event.getValue().equals("Kutahya")) selectCityDistrict.setItems(kutahya);
            else if (event.getValue().equals("Malatya")) selectCityDistrict.setItems(malatya);
            else if (event.getValue().equals("Manisa")) selectCityDistrict.setItems(manisa);
            else if (event.getValue().equals("Mardin")) selectCityDistrict.setItems(mardin);
            else if (event.getValue().equals("Mersin")) selectCityDistrict.setItems(mersin);
            else if (event.getValue().equals("Mugla")) selectCityDistrict.setItems(mugla);
            else if (event.getValue().equals("Mus")) selectCityDistrict.setItems(mus);
            else if (event.getValue().equals("Nevsehir")) selectCityDistrict.setItems(nevsehir);
            else if (event.getValue().equals("Nigde")) selectCityDistrict.setItems(nigde);
            else if (event.getValue().equals("Ordu")) selectCityDistrict.setItems(ordu);
            else if (event.getValue().equals("Osmaniye")) selectCityDistrict.setItems(osmaniye);
            else if (event.getValue().equals("Rize")) selectCityDistrict.setItems(rize);
            else if (event.getValue().equals("Sakarya")) selectCityDistrict.setItems(sakarya);
            else if (event.getValue().equals("Samsun")) selectCityDistrict.setItems(samsun);
            else if (event.getValue().equals("Sanliurfa")) selectCityDistrict.setItems(sanliurfa);
            else if (event.getValue().equals("Siirt")) selectCityDistrict.setItems(siirt);
            else if (event.getValue().equals("Sinop")) selectCityDistrict.setItems(sinop);
            else if (event.getValue().equals("Sirnak")) selectCityDistrict.setItems(sirnak);
            else if (event.getValue().equals("Sivas")) selectCityDistrict.setItems(sivas);
            else if (event.getValue().equals("Tekirdag")) selectCityDistrict.setItems(tekirdag);
            else if (event.getValue().equals("Tokat")) selectCityDistrict.setItems(tokat);
            else if (event.getValue().equals("Trabzon")) selectCityDistrict.setItems(trabzon);
            else if (event.getValue().equals("Tunceli")) selectCityDistrict.setItems(tunceli);
            else if (event.getValue().equals("Usak")) selectCityDistrict.setItems(usak);
            else if (event.getValue().equals("Van")) selectCityDistrict.setItems(van);
            else if (event.getValue().equals("Yalova")) selectCityDistrict.setItems(yalova);
            else if (event.getValue().equals("Yozgat")) selectCityDistrict.setItems(yozgat);
            else if (event.getValue().equals("Zonguldak")) selectCityDistrict.setItems(zonguldak);

        });

        //City District Last
        //-----------------------------------------------------------------------------------------------------------------
        //Adress,Price,Description Begin
        TextField textAddress = new TextField("Adres", "Adres girin");

        TextField textPrice = new TextField("Fiyat", "Fiyat??n?? girin");

        TextField textDescription = new TextField("A????klama", "??r??n a????klamas??n?? girin");
        //Adress,Price,Description Last
        //-----------------------------------------------------------------------------------------------------------------
        //Category Begin
        ComboBox selectCategory = new ComboBox<>("Kategori");
        String[] categories = new String[]{"Elektronik", "Ev E??yalar??", "Araba"};

        selectCategory.setItems(categories);
        selectCategory.setPlaceholder("Kategori Se??in");

        //Category Last

        //FormLayout
        FormLayout formLayout = new FormLayout();
        formLayout.add(selectCategory, textPrice, selectCity, selectCityDistrict, textAddress, textDescription, /*valueDatePicker,*/ upload, image);

        //FormLayout Last
        //Properties Last

        //-----------------------------------------------------------------------------------------------------------------

        //Filter Begin
        Button btnFilter = new Button("Ara", VaadinIcon.SEARCH.create());

        TextField textFilter = new TextField();
        textFilter.setPlaceholder("??r??n girin");

        HorizontalLayout filterGroup = new HorizontalLayout();

        filterGroup.add(textFilter, btnFilter);

        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(textFilter.getValue());
            textFilter.setValue("");
        });
        //F??lter Last
        //-----------------------------------------------------------------------------------------------------------------
        // Properties Save-Cancel Begin
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        Button btnSave = new Button("Kaydet");
        Button btnCancel = new Button("????k");

        //Product product = new Product();
        btnSave.addClickListener(buttonClickEvent -> {

            Category category = new Category();
            User user = new User();
            Product product = new Product();


            category.setCategoryType(selectCategory.getValue().toString());
            selectCategory.setValue("");
            product.setPrice(Double.valueOf(textPrice.getValue()));
            textPrice.setValue("");
            product.setCityDistrict(selectCityDistrict.getValue().toString());
            selectCityDistrict.setValue("");
            categoryService.save(category);

            product.setCategory(category);
            product.setAddress(textAddress.getValue());
            textAddress.setValue("");
            product.setDescription(textDescription.getValue());
            textDescription.setValue("");
            product.setDate(valueDatePicker.getValue());
            product.setCity(String.valueOf(selectCity.getValue()));
            selectCity.setValue("");

            if (!(image.getSrc().equals(""))) {
                try {
                    product.setImage(IOUtils.toByteArray(buffer.getInputStream(image.getTitle().get())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                product.setImageFileName(image.getTitle().get());
            } else {
                product.setImage("".getBytes(StandardCharsets.UTF_8));
                product.setImageFileName("");
            }

            image.setSrc("");
            image.setTitle("");

            product.setId(itemIdForEdition);

            if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId") != null) {
                user = userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get();
                product.setUser(user);
            }

            productService.save(product);
            itemIdForEdition = 0L;
            refreshData(textFilter.getValue());

            dialog.close();
        });

        btnCancel.addClickListener(buttonClickEvent -> {
            selectCategory.setValue("");
            textPrice.setValue("");
            selectCityDistrict.setValue("");
            textAddress.setValue("");
            textDescription.setValue("");
            selectCity.setValue("");
            image.setSrc("");
            image.setTitle("");
            dialog.close();
        });

        horizontalLayout.add(btnSave, btnCancel);
        dialog.add(formLayout, horizontalLayout);

        // Properties Save-Cancel Last

        //-----------------------------------------------------------------------------------------------------------------

        //ProductView and ClickItem
        Dialog clickDialog = new Dialog();
        clickDialog.setHeight("1000px");
        clickDialog.setWidth("500px");
        //Add Product click to dialog
        Button btnEkle = new Button("??r??n Ekle", VaadinIcon.INSERT.create());
        btnEkle.addClickListener(buttonClickEvent -> {
            dialog.open();

        });

        grid.addItemClickListener(productItemClickEvent -> {
                    //G??r??nt??lenme Sayisi
                    int x = productItemClickEvent.getItem().getNumberOfViews();
                    x = x + 1;
                    productItemClickEvent.getItem().setNumberOfViews(x);
                    productService.save(productItemClickEvent.getItem());//productItemClickEvent.getItem()=>return select row product
                    //refreshData();

                    TextArea txtUser1 = new TextArea();
                    txtUser1.setLabel("??sim");
                    txtUser1.setValue(productItemClickEvent.getItem().getUser().getFirstName().toString());
                    txtUser1.setReadOnly(true);

                    TextArea txtCity1 = new TextArea();
                    txtCity1.setLabel("??ehir");
                    txtCity1.setValue(productItemClickEvent.getItem().getCity().toString());
                    txtCity1.setReadOnly(true);

                    TextArea txtCategory1 = new TextArea();
                    txtCategory1.setLabel("Kategori");
                    txtCategory1.setValue(productItemClickEvent.getItem().getCategory().getCategoryType().toString());
                    txtCategory1.setReadOnly(true);

                    TextArea txtAdress1 = new TextArea();
                    txtAdress1.setLabel("Adres");
                    txtAdress1.setValue(productItemClickEvent.getItem().getAddress().toString());
                    txtAdress1.setReadOnly(true);

                    TextArea txtCityDistrict1 = new TextArea();
                    txtCityDistrict1.setLabel("??l??e");
                    txtCityDistrict1.setValue(productItemClickEvent.getItem().getCityDistrict().toString());
                    txtCityDistrict1.setReadOnly(true);

                    TextArea txtPrice1 = new TextArea();
                    txtPrice1.setLabel("Fiyat");
                    txtPrice1.setValue(productItemClickEvent.getItem().getPrice().toString());
                    txtPrice1.setReadOnly(true);

                    TextArea txtDescription1 = new TextArea();
                    txtDescription1.setLabel("A????klama");
                    txtDescription1.setValue(productItemClickEvent.getItem().getDescription().toString());
                    txtDescription1.setReadOnly(true);

                    TextArea txtNumberofView1 = new TextArea();
                    txtNumberofView1.setLabel("G??r??nt??lenme Say??s??");
                    txtNumberofView1.setValue(productItemClickEvent.getItem().getNumberOfViews().toString());
                    txtNumberofView1.setReadOnly(true);


                    Image image1 = new Image();
                    image1.setWidth("100px");
                    image1.setHeight("100px");

                    if (productItemClickEvent.getItem().getImageFileName() != null) {
                        StreamResource resource = new StreamResource(productItemClickEvent.getItem().getImageFileName(), () -> new ByteArrayInputStream((productItemClickEvent.getItem().getImage())));
                        image1.setSrc(resource);

                    } else {
                        image1.setSrc("");
                    }



                    Button updateClickBtn = new Button("G??ncelle");
                    updateClickBtn.addClickListener(buttonClickEvent -> {
                        itemIdForEdition = productItemClickEvent.getItem().getId();
                        selectCity.setValue(productItemClickEvent.getItem().getCity());
                        selectCategory.setValue(productItemClickEvent.getItem().getCategory().getCategoryType());
                        selectCityDistrict.setValue(productItemClickEvent.getItem().getCityDistrict());
                        textAddress.setValue(productItemClickEvent.getItem().getAddress());
                        textDescription.setValue(productItemClickEvent.getItem().getDescription());
                        textPrice.setValue(productItemClickEvent.getItem().getPrice().toString());


                        if (productItemClickEvent.getItem().getImageFileName() != null) {
                            StreamResource resource = new StreamResource(productItemClickEvent.getItem().getImageFileName(), () -> new ByteArrayInputStream((productItemClickEvent.getItem().getImage())));

                            image.setSrc(resource);
                            image.setWidth("100px");
                            image.setHeight("100px");
                        } else {
                            image.setSrc("");
                        }

                        dialog.open();//formlayout
                        //refreshData();
                    });

                    clickDialog.open();
                    Button cancelclickBtn = new Button("Kapat");

                    Button messageClickBtn = new Button("Mesaj");

                    Button sendMessageBtn = new Button("G??nder");
                    Button cancelMessageBtn = new Button("Kapat");
                    TextArea textMessage = new TextArea();



                    messageClickBtn.addClickListener(buttonClickEvent -> {
                        messageDialog.add(textMessage, sendMessageBtn, cancelMessageBtn);
                        messageDialog.open();


                        textMessage.setWidth("200px");
                        textMessage.setWidth("200px");
                    });
                    sendMessageBtn.addClickListener(buttonClickEvent -> {
                        Message message1 = new Message();
                        message1.setMessageText(textMessage.getValue());
                        message1.setUser(productItemClickEvent.getItem().getUser());
                        message1.setGonderenUser(userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get());

                        messageService.save(message1);
                        textMessage.setValue("");
                        messageDialog.remove(textMessage, sendMessageBtn, cancelMessageBtn);
                        messageDialog.close();
                    });

                    //message cancel
                    cancelMessageBtn.addClickListener(buttonClickEvent -> {
                        messageDialog.remove(textMessage, sendMessageBtn, cancelMessageBtn);
                        textMessage.setValue("");
                        messageDialog.close();
                    });

                    clickDialog.add(txtUser1, txtCategory1, txtCity1, txtCityDistrict1, txtAdress1, txtPrice1, txtDescription1, txtNumberofView1, image1, cancelclickBtn);
                    //kendi giri??e mesaj atamas??n
                    if (productItemClickEvent.getItem().getUser().getId() != userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId()) {
                        clickDialog.add(messageClickBtn);
                    } else {
                        clickDialog.add(updateClickBtn);
                    }

                    cancelclickBtn.addClickListener(buttonClickEvent -> {
                        clickDialog.remove(txtUser1, txtCity1, txtCategory1, txtAdress1, txtCityDistrict1, txtPrice1, txtDescription1, txtNumberofView1, image1, cancelclickBtn);
                        //kendi giri??inde dialogdan silinsin.
                        if (productItemClickEvent.getItem().getUser().getId() != userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId()) {
                            clickDialog.remove(messageClickBtn);
                        } else {
                            clickDialog.remove(updateClickBtn);
                        }


                        clickDialog.close();
                    });
                    //refreshData();

                }
        );
        Button btnMyProduct = new Button("??r??nlerim");
        btnMyProduct.addClickListener(buttonClickEvent -> {
            refreshData(userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId());
        });
        Button btnAllProduct = new Button("T??m ??r??nler");
        btnAllProduct.addClickListener(buttonClickEvent -> {
            refreshData();
        });
        //CategoryFilter begin
        Button btnCategoryFilter = new Button("Kategori Ara", VaadinIcon.FILTER.create());
        ComboBox comboBoxCategoryFilter = new ComboBox<>();
        comboBoxCategoryFilter.setPlaceholder("Kategori Se??in");
        comboBoxCategoryFilter.setItems(categories);

        HorizontalLayout btnCategoryFilterGruop = new HorizontalLayout();

        btnCategoryFilterGruop.add(comboBoxCategoryFilter, btnCategoryFilter);

        btnCategoryFilter.addClickListener(buttonClickEvent -> {
            refreshCategoryData(comboBoxCategoryFilter.getValue().toString());
            comboBoxCategoryFilter.setValue("");
        });
        //category filter last

        //My Message
        Button myMessageButton = new Button("Mesajlar??m ");
        Button cancelMyMessageButton = new Button("Kapat");

        Dialog myMessageDialog = new Dialog();

        TextArea textAreaMessage = new TextArea();
        textAreaMessage.setReadOnly(true);
        textAreaMessage.setValue(String.valueOf(refreshMessageData(userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId())));


        myMessageDialog.add(textAreaMessage, cancelMyMessageButton);
        myMessageButton.addClickListener(buttonClickEvent -> {
            myMessageDialog.open();
        });

        cancelMyMessageButton.addClickListener(buttonClickEvent -> {
            myMessageDialog.close();
        });
        //my message last
        Button btnLogout = new Button("????k???? Yap");
        btnLogout.addClickListener(buttonClickEvent -> {
            UI.getCurrent().getPage().setLocation("/firstpage");
        });
        HorizontalLayout horizontalLayoutAllandMyProduct = new HorizontalLayout();
        horizontalLayoutAllandMyProduct.add(btnEkle, btnMyProduct, btnAllProduct, myMessageButton, btnLogout);

        HorizontalLayout horizontalLayoutbtnCategoryFilterandFilterGroup = new HorizontalLayout();
        horizontalLayoutbtnCategoryFilterandFilterGroup.add(btnCategoryFilterGruop, filterGroup);

        grid.setColumns("user.firstName", "category.categoryType", "city", "cityDistrict", "address", "price", "description", "date", "numberOfViews");
        grid.setHeightByRows(true);
        grid.addComponentColumn(item -> imageShow(grid, item)).setAutoWidth(true);

        refreshData();
        add(horizontalLayoutAllandMyProduct, horizontalLayoutbtnCategoryFilterandFilterGroup, grid);
    }

    private void refreshData() {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getList());
        grid.setItems(productList);
    }

    private void refreshData(String filter) {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getList(filter));
        grid.setItems(productList);
    }

    private void refreshData(Long id) {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getList(id));
        grid.setItems(productList);
    }

    private void refreshCategoryData(String categoryType) {
        List<Product> productList = new ArrayList<>();
        productList.addAll(productService.getListCategory(categoryType));
        grid.setItems(productList);
    }

    private List<String> refreshMessageData(Long id) {
        List<Message> messageList = new ArrayList<>();
        List<String> messageText = new ArrayList<>();
        messageList.addAll(messageService.getList(id));
        for (var mes : messageList) {
            messageText.add(mes.getMessageText());
        }

        return messageText;
    }

    private Image imageShow(Grid<Product> grid, Product item) {

        Image image2 = new Image();
        image2.setSrc("");
        image2.setWidth("100px");
        image2.setHeight("100px");
        if (item.getImageFileName() != null) {
            StreamResource resource = new StreamResource(item.getImageFileName(), () -> new ByteArrayInputStream((item.getImage())));
            image2.setSrc(resource);
        }
        return image2;
    }
}
package com.example.application.views.main;

import com.example.application.models.Category;
import com.example.application.models.Message;
import com.example.application.models.Product;
import com.example.application.models.User;
import com.example.application.services.*;
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


        upload.setReceiver(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            try {

                byte[] imageBytes = IOUtils.toByteArray(buffer.getInputStream(event.getFileName()));
                StreamResource resource = new StreamResource(event.getFileName(), () -> new ByteArrayInputStream(imageBytes));
                image.setSrc(resource);
                image.setWidth("200px");
                image.setHeight("200px");
                image.setTitle(event.getFileName());
                output.removeAll();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        upload.getElement().addEventListener("file-remove", event -> {
            output.removeAll();
            //image.removeAll();
        });


        //image last
        //City Begin
        ComboBox selectCity = new ComboBox<>("Şehir");
        String[] cities = new String[]{"Adana", "Adiyaman", "Afyon", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydin",
                "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa", "Canakkale", "Cankiri",
                "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir", "Gaziantep", "Giresun",
                "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul", "Izmir", "Kahramanmaras", "Karabuk", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa",
                "Mardin", "Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt",
                "Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"};

        selectCity.setItems(cities);
        selectCity.setPlaceholder("İl Seçiniz");
        //City Last
        //-----------------------------------------------------------------------------------------------------------------
        //City District Begin
        ComboBox selectCityDistrict = new ComboBox<>("İlçe");
        selectCityDistrict.setPlaceholder("İlçe Seçiniz");

        String[] adana = new String[]{"Aladağ", "Ceyhan", "Çukurova", "Feke", "İmamoğlu", "Karaisalı", "Karataş", "Kozan",
                "Pozantı", "Saimbeyli", "Sarıçam", "Seyhan", "Tufanbeyli", "Yumurtalık", "Yüreğir"};

        String[] adiyaman = new String[]{"Adıyaman (İl merkezi)", "Besni", "Çelikhan", "Gerger", "Gölbaşı", "Kâhta", "Samsat", "Sincik", "Tut"};
        String[] afyon = new String[]{"Afyonkarahisar (İl merkezi)", "Başmakçı", "Bayat", "Bolvadin", "Çay", "Çobanlar", "Dazkırı", "Dinar", "Emirdağ",
                "Evciler", "Hocalar", "İhsaniye", "İscehisar", "Kızılören", "Sandıklı", "Sinanpaşa", "Sultandağı", "Şuhut"};
        String[] agri = new String[]{"Ağrı (il merkezi)", "Diyadin", "Doğubayazıt", "Eleşkirt", "Hamur", "Patnos", "Taşlıçay", "Tutak"};
        String[] aksaray = new String[]{"Aksaray (İl merkezi)", "Ağaçören", "Eskil", "Gülağaç", "Güzelyurt", "Ortaköy", "Sarıyahşi", "Sultanhanı"};
        String[] amasya = new String[]{"Amasya (il merkezi)", "Göynücek", "Gümüşhacıköy", "Hamamözü", "Merzifon", "Suluova", "Taşova"};
        String[] ankara = new String[]{"Akyurt", "Altındağ", "Ayaş", "Balâ", "Beypazarı", "Çamlıdere", "Çankaya", "Çubuk", "Elmadağ", "Etimesgut",
                "Evren", "Gölbaşı", "Güdül", "Haymana", "Kahramankazan", "Kalecik", "Keçiören", "Kızılcahamam", "Mamak",
                "Nallıhan", "Polatlı", "Pursaklar", "Sincan", "Şereflikoçhisar", "Yenimahalle"};
        String[] antalya = new String[]{"Akseki", "Aksu", "Alanya", "Demre", "Döşemealtı", "Elmalı", "Finike", "Gazipaşa", "Gündoğmuş", "İbradı",
                "Kaş", "Kemer", "Kepez", "Konyaaltı", "Korkuteli", "Kumluca", "Manavgat", "Muratpaşa", "Serik"};
        String[] ardahan = new String[]{"Ardahan (il merkezi)", "Çıldır", "Damal", "Göle", "Hanak", "Posof"};
        String[] artvin = new String[]{"Artvin (il merkezi)", "Ardanuç", "Arhavi", "Borçka", "Hopa", "Kemalpaşa", "Murgul", "Şavşat", "Yusufeli"};
        String[] aydin = new String[]{"Bozdoğan", "Buharkent", "Çine", "Germencik", "İncirliova", "Karacasu", "Koçarlı", "Köşk", "Kuşadası", "Kuyucak",
                "Nazilli", "Söke", "Sultanhisar", "Yenipazar"};
        String[] balikesir = new String[]{"Altıeylül", "Ayvalık", "Balya", "Bandırma", "Bigadiç", "Burhaniye", "Dursunbey", "Edremit", "Erdek", "Gömeç", "Gönen",
                "Havran", "İvrindi", "Karesi", "Kepsut", "Manyas", "Marmara", "Savaştepe", "Sındırgı", "Susurluk"};
        String[] bartin = new String[]{"Bartın (il merkezi)", "Amasra", "Kurucaşile", "Ulus"};
        String[] batman = new String[]{"Batman (il merkezi)", "Beşiri", "Gercüş", "Hasankeyf", "Kozluk", "Sason"};
        String[] bayburt = new String[]{"Bayburt (il merkezi)", "Demirören ", "Aydıntepe "};
        String[] bilecik = new String[]{"Bilecik (il merkezi)", "Bozüyük", "Gölpazarı", "İnhisar", "Osmaneli", "Pazaryeri", "Söğüt", "Yenipazar"};
        String[] bingöl = new String[]{"Bingöl (il merkezi)", "Adaklı", "Genç", "Karlıova", "Kiğı", "Solhan", "Yayladere", "Yedisu"};
        String[] bitlis = new String[]{"Bitlis (il merkezi)", "Adilcevaz", "Ahlat", "Güroymak", "Hizan", "Mutki", "Tatvan"};
        String[] bolu = new String[]{"Bolu (il merkezi)", "Dörtdivan", "Gerede", "Göynük", "Kıbrıscık", "Mengen", "Mudurnu", "Seben"};
        String[] burdur = new String[]{"Burdur (il merkezi)", "Ağlasun", "Altınyayla", "Bucak", "Çavdır", "Çeltikçi", "Gölhisar", "Karamanlı"};
        String[] bursa = new String[]{"Büyükorhan", "Gemlik", "Gürsu", "Harmancık", "İnegöl", "İznik", "Karacabey", "Keles", "Kestel", "Mudanya", "Mustafakemalpaşa",
                "Nilüfer", "Orhaneli", "Orhangazi", "Osmangazi", "Yenişehir", "Yıldırım"};
        String[] canakkale = new String[]{"Çanakkale (il merkezi)", "Ayvacık", "Bayramiç", "Biga", "Bozcaada", "Çan", "Eceabat", "Ezine", "Gelibolu", "Gökçeada",
                "Lapseki", "Yenice"};
        String[] cankiri = new String[]{"Çankırı (il merkezi)", "Atkaracalar", "Bayramören", "Çerkeş", "Eldivan", "Ilgaz", "Kızılırmak", "Korgun", "Kurşunlu", "Orta",
                "Şabanözü", "Yapraklı"};
        String[] corum = new String[]{"Çorum (il merkezi)", "Alaca", "Bayat", "Boğazkale", "Dodurga", "İskilip", "Kargı", "Laçin", "Mecitözü", "Oğuzlar", "Ortaköy", "Osmancık",
                "Sungurlu", "Uğurludağ"};
        String[] denizli = new String[]{"Acıpayam", "Babadağ", "Baklan", "Bekilli", "Beyağaç", "Bozkurt", "Buldan", "Çal", "Çameli", "Çardak", "Çivril", "Güney", "Honaz", "Kale",
                "Merkezefendi", "Pamukkale", "Sarayköy", "Serinhisar", "Tavas"};
        String[] diyarbakir = new String[]{"Bağlar", "Bismil", "Çermik", "Çınar", "Çüngüş", "Dicle", "Eğil", "Ergani", "Hani", "Hazro", "Kayapınar", "Kocaköy", "Kulp", "Lice", "Silvan",
                "Sur", "Yenişehir"};
        String[] düzce = new String[]{"Düzce (il merkezi)", "Akçakoca", "Cumayeri", "Çilimli", "Gölyaka", "Gümüşova", "Kaynaşlı", "Yığılca"};
        String[] edirne = new String[]{"Edirne (il merkezi)", "Enez", "Havsa", "İpsala", "Keşan", "Lalapaşa", "Meriç", "Süloğlu", "Uzunköprü"};
        String[] elazig = new String[]{"Elâzığ (il merkezi)", "Ağın", "Alacakaya", "Arıcak", "Baskil", "Karakoçan", "Keban", "Kovancılar", "Maden", "Palu", "Sivrice"};
        String[] erzincan = new String[]{"Erzincan (il merkezi)", "Çayırlı", "İliç", "Kemah", "Kemaliye", "Otlukbeli", "Refahiye", "Tercan", "Üzümlü"};
        String[] erzurum = new String[]{"Aşkale", "Aziziye", "Çat", "Hınıs", "Horasan", "İspir", "Karaçoban", "Karayazı", "Köprüköy", "Narman", "Oltu", "Olur", "Palandöken", "Pasinler",
                "Pazaryolu", "Şenkaya", "Tekman", "Tortum", "Uzundere", "Yakutiye"};
        String[] eskisehir = new String[]{"Alpu", "Beylikova", "Çifteler", "Günyüzü", "Han", "İnönü", "Mahmudiye", "Mihalgazi", "Mihalıççık", "Odunpazarı", "Sarıcakaya", "Seyitgazi",
                "Sivrihisar", "Tepebaşı"};
        String[] gaziantep = new String[]{"Araban", "İslahiye", "Karkamış", "Nizip", "Nurdağı", "Oğuzeli", "Şahinbey", "Şehitkamil", "Yavuzeli"};
        String[] giresun = new String[]{"Piraziz", "Bulancak", "Giresun (il merkezi)", "Keşap", "Espiye", "Tirebolu", "Görele", "Eynesil"};
        String[] gumushane = new String[]{"Gümüşhane (il merkezi)", "Kelkit", "Köse", "Kürtün", "Şiran", "Torul"};
        String[] hakkari = new String[]{"Hakkâri (il merkezi)", "Çukurca", "Şemdinli", "Yüksekova", "Derecik "};
        String[] hatay = new String[]{"Altınözü", "Antakya", "Arsuz", "Belen", "Defne", "Dörtyol", "Erzin", "Hassa", "İskenderun", "Kırıkhan", "Kumlu", "Payas", "Reyhanlı",
                "Samandağ", "Yayladağı"};
        String[] igdir = new String[]{"Iğdır (il merkezi)", "Aralık", "Karakoyunlu", "Tuzluca"};
        String[] isparta = new String[]{"Isparta (İl merkezi)", "Aksu", "Atabey", "Eğirdir", "Gelendost", "Gönen", "Keçiborlu", "Senirkent", "Sütçüler", "Şarkikaraağaç",
                "Uluborlu", "Yalvaç", "Yenişarbademli"};
        String[] istanbul = new String[]{"Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy", "Başakşehir", "Bayrampaşa", "Beşiktaş",
                "Beykoz", "Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüpsultan", "Fatih", "Gaziosmanpaşa", "Güngören",
                "Kadıköy", "Kâğıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Silivri", "Sultanbeyli", "Sultangazi", "Şile", "Şişli",
                "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"};
        String[] izmir = new String[]{"Aliağa", "Balçova", "Bayındır", "Bayraklı", "Bergama", "Beydağ", "Bornova", "Buca", "Çeşme", "Çiğli", "Dikili", "Foça", "Gaziemir",
                "Güzelbahçe", "Karabağlar", "Karaburun", "Karşıyaka", "Kemalpaşa", "Kınık", "Kiraz", "Konak", "Menderes", "Menemen", "Narlıdere", "Ödemiş", "Seferihisar",
                "Selçuk", "Tire", "Torbalı", "Urla"};
        String[] kahramanmaras = new String[]{"Afşin", "Andırın", "Çağlayancerit", "Dulkadiroğlu", "Ekinözü", "Elbistan", "Göksun", "Nurhak", "Onikişubat", "Pazarcık",
                "Türkoğlu"};
        String[] karabuk = new String[]{"Karabük (İl merkezi)", "Eflani", "Eskipazar", "Ovacık", "Safranbolu", "Yenice"};
        String[] karaman = new String[]{"Karaman (il merkezi)", "Ayrancı", "Başyayla", "Ermenek", "Kazımkarabekir", "Sarıveliler"};
        String[] kars = new String[]{"Kars (il merkezi)", "Akyaka", "Arpaçay", "Digor", "Kağızman", "Sarıkamış", "Selim", "Susuz"};
        String[] kastamonu = new String[]{"Merkez", "Abana", "Ağlı", "Araç", "Azdavay", "Bozkurt", "Cide", "Çatalzeytin", "Daday", "Devrekani", "Doğanyurt", "Hanönü",
                "İhsangazi", "İnebolu", "Küre", "Pınarbaşı", "Seydiler", "Şenpazar", "Taşköprü", "Tosya"};
        String[] kayseri = new String[]{"Akkışla", "Bünyan", "Develi", "Felahiye", "Hacılar", "İncesu", "Kocasinan", "Melikgazi", "Özvatan", "Pınarbaşı", "Sarıoğlan",
                "Sarız", "Talas", "Tomarza", "Yahyalı", "Yeşilhisar"};
        String[] kilis = new String[]{"Kilis (İl merkezi)", "Elbeyli", "Musabeyli", "Polateli"};
        String[] kirikkale = new String[]{"Kırıkkale (il merkezi)", "Bahşili", "Balışeyh", "Çelebi", "Delice", "Karakeçili", "Keskin", "Sulakyurt", "Yahşihan"};
        String[] kirklareli = new String[]{"Babaeski", "Demirköy", "Kofçaz", "Lüleburgaz", "Pehlivanköy", "Pınarhisar", "Vize'"};
        String[] kirsehir = new String[]{"Kırşehir (İl merkezi)", "Akçakent", "Akpınar", "Boztepe", "Çiçekdağı", "Kaman", "Mucur"};
        String[] kocaeli = new String[]{"Başiskele", "Çayırova", "Darıca", "Derince", "Dilovası", "Gebze", "Gölcük", "İzmit", "Kandıra", "Karamürsel", "Kartepe",
                "Körfez"};
        String[] konya = new String[]{"Ahırlı", "Akören", "Akşehir", "Altınekin", "Beyşehir", "Bozkır", "Cihanbeyli", "Çeltik", "Çumra", "Derbent", "Derebucak",
                "Doğanhisar", "Emirgazi", "Ereğli", "Güneysınır", "Hadim", "Halkapınar", "Hüyük", "Ilgın", "Kadınhanı", "Karapınar", "Karatay", "Kulu", "Meram", "Sarayönü",
                "Selçuklu", "Seydişehir", "Taşkent", "Tuzlukçu", "Yalıhüyük", "Yunak"};
        String[] kutahya = new String[]{"Kütahya (İl merkezi)", "Altıntaş", "Aslanapa", "Çavdarhisar", "Domaniç", "Dumlupınar", "Emet", "Gediz", "Hisarcık", "Pazarlar",
                "Şaphane", "Simav", "Tavşanlı"};
        String[] malatya = new String[]{"Akçadağ", "Arapgir", "Arguvan", "Battalgazi", "Darende", "Doğanşehir", "Doğanyol", "Hekimhan", "Kale", "Kuluncak", "Pütürge",
                "Yazıhan", "Yeşilyurt"};
        String[] manisa = new String[]{"Ahmetli", "Akhisar", "Alaşehir", "Demirci", "Gölmarmara", "Gördes", "Kırkağaç", "Köprübaşı", "Kula", "Salihli", "Sarıgöl",
                "Saruhanlı", "Selendi", "Soma", "Şehzadeler", "Turgutlu", "Yunusemre"};
        String[] mardin = new String[]{"Artuklu", "Dargeçit", "Derik", "Kızıltepe", "Mazıdağı", "Midyat", "Nusaybin", "Ömerli", "Savur", "Yeşilli"};
        String[] mersin = new String[]{"Akdeniz", "Anamur", "Aydıncık", "Bozyazı", "Çamlıyayla", "Erdemli", "Gülnar", "Mezitli", "Mut", "Silifke", "Tarsus", "Toroslar",
                "Yenişehir"};
        String[] mugla = new String[]{"Bodrum", "Dalaman", "Datça", "Fethiye", "Kavaklıdere", "Köyceğiz", "Marmaris", "Menteşe", "Milas", "Ortaca", "Seydikemer", "Ula",
                "Yatağan"};
        String[] mus = new String[]{"Muş (İl merkezi)", "Bulanık", "Hasköy", "Korkut", "Malazgirt", "Varto"};
        String[] nevsehir = new String[]{"Nevşehir (merkez ilçe)", "Acıgöl", "Avanos", "Derinkuyu", "Gülşehir", "Hacıbektaş", "Kozaklı", "Ürgüp"};
        String[] nigde = new String[]{"Niğde (İl merkezi)", "Altunhisar", "Bor", "Çamardı", "Çiftlik", "Ulukışla"};
        String[] ordu = new String[]{"Akkuş", "Altınordu", "Aybastı", "Çamaş", "Çatalpınar", "Çaybaşı", "Fatsa", "Gölköy", "Gülyalı", "İkizce", "Kabadüz", "Kabataş",
                "Korgan", "Kumru", "Mesudiye", "Perşembe", "Ulubey", "Ünye"};
        String[] osmaniye = new String[]{"Osmaniye (il merkezi)", "Bahçe", "Düziçi", "Hasanbeyli", "Kadirli", "Sumbas", "Toprakkale"};
        String[] rize = new String[]{"Rize (il merkezi)", "Ardeşen", "Çamlıhemşin", "Çayeli", "Derepazarı", "Fındıklı", "Güneysu", "Hemşin", "İkizdere", "İyidere",
                "Kalkandere", "Pazar"};
        String[] sakarya = new String[]{"Adapazarı", "Akyazı", "Arifiye", "Erenler", "Ferizli", "Geyve", "Hendek", "Karapürçek", "Karasu", "Kaynarca", "Kocaali",
                "Pamukova", "Sapanca", "Serdivan", "Söğütlü", "Taraklı"};
        String[] samsun = new String[]{"19 Mayıs", "Alaçam", "Asarcık", "Atakum", "Ayvacık", "Bafra", "Canik", "Çarşamba", "Havza", "İlkadım", "Kavak", "Ladik",
                "Salıpazarı", "Tekkeköy", "Terme", "Vezirköprü", "Yakakent"};
        String[] sanliurfa = new String[]{"Akçakale", "Birecik", "Bozova", "Ceylanpınar", "Halfeti", "Harran", "Hilvan", "Siverek", "Suruç", "Viranşehir", "Karaköprü",
                "Haliliye", "Eyyübiye"};
        String[] siirt = new String[]{"Siirt (İl merkezi)", "Baykan", "Eruh", "Kurtalan", "Pervari", "Şirvan", "Tillo"};
        String[] sinop = new String[]{"Sinop (il merkezi)", "Ayancık", "Boyabat", "Dikmen", "Durağan", "Erfelek", "Gerze", "Saraydüzü", "Türkeli"};
        String[] sirnak = new String[]{"Şırnak (il merkezi)", "Beytüşşebap", "Cizre", "Güçlükonak", "İdil", "Silopi", "Uludere"};
        String[] sivas = new String[]{"Sivas (İl merkezi)", "Akıncılar", "Altınyayla", "Divriği", "Doğanşar", "Gemerek", "Gölova", "Gürün", "Hafik", "İmranlı", "Kangal",
                "Koyulhisar", "Suşehri", "Şarkışla", "Ulaş", "Yıldızeli", "Zara"};
        String[] tekirdag = new String[]{"Süleymanpaşa", "Çerkezköy", "Çorlu", "Ergene", "Hayrabolu", "Kapaklı", "Malkara", "Marmaraereğlisi", "Muratlı", "Saray",
                "Şarköy"};
        String[] tokat = new String[]{"Tokat (il merkezi)", "Almus", "Artova", "Başçiftlik", "Erbaa", "Niksar", "Pazar", "Reşadiye", "Sulusaray", "Turhal", "Yeşilyurt",
                "Zile"};
        String[] trabzon = new String[]{"Akçaabat", "Araklı", "Arsin", "Beşikdüzü", "Çarşıbaşı", "Çaykara", "Dernekpazarı", "Düzköy", "Hayrat", "Köprübaşı", "Maçka",
                "Of", "Ortahisar", "Sürmene", "Şalpazarı", "Tonya", "Vakfıkebir", "Yomra"};
        String[] tunceli = new String[]{"Tunceli (il merkezi)", "Çemişgezek", "Hozat", "Mazgirt", "Nazımiye", "Ovacık", "Pertek", "Pülümür"};
        String[] usak = new String[]{"Uşak (il merkezi)", "Banaz", "Eşme", "Karahallı", "Sivaslı", "Ulubey"};
        String[] van = new String[]{"Bahçesaray", "Başkale", "Çaldıran", "Çatak", "Edremit", "Erciş", "Gevaş", "Gürpınar", "İpekyolu", "Muradiye", "Özalp", "Saray",
                "Tuşba"};
        String[] yalova = new String[]{"Yalova (il merkezi)", "Altınova", "Armutlu", "Çınarcık", "Çiftlikköy", "Termal"};
        String[] yozgat = new String[]{"Yozgat (il merkezi)", "Akdağmadeni", "Aydıncık", "Boğazlıyan", "Çandır", "Çayıralan", "Çekerek", "Kadışehri", "Saraykent",
                "Sarıkaya", "Sorgun", "Şefaatli", "Yenifakılı", "Yerköy"};
        String[] zonguldak = new String[]{"Zonguldak (il merkezi)", "Alaplı", "Çaycuma", "Devrek", "Gökçebey", "Karadeniz Ereğli", "Kilimli", "Kozlu"};

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
            else if (event.getValue().equals("Bingol")) selectCityDistrict.setItems(bingöl);
            else if (event.getValue().equals("Bitlis")) selectCityDistrict.setItems(bitlis);
            else if (event.getValue().equals("Bolu")) selectCityDistrict.setItems(bolu);
            else if (event.getValue().equals("Burdur")) selectCityDistrict.setItems(burdur);
            else if (event.getValue().equals("Bursa")) selectCityDistrict.setItems(bursa);
            else if (event.getValue().equals("Canakkale")) selectCityDistrict.setItems(canakkale);
            else if (event.getValue().equals("Cankiri")) selectCityDistrict.setItems(cankiri);
            else if (event.getValue().equals("Corum")) selectCityDistrict.setItems(corum);
            else if (event.getValue().equals("Denizli")) selectCityDistrict.setItems(denizli);
            else if (event.getValue().equals("Diyarbakir")) selectCityDistrict.setItems(diyarbakir);
            else if (event.getValue().equals("Duzce")) selectCityDistrict.setItems(düzce);
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

        TextField textPrice = new TextField("Fiyat", "Fiyatını girin");

        TextField textDescription = new TextField("Açıklama", "Ürün açıklamasını girin");
        //Adress,Price,Description Last
        //-----------------------------------------------------------------------------------------------------------------
        //Category Begin
        ComboBox selectCategory = new ComboBox<>("Kategori");
        String[] categories = new String[]{"Elektronik", "Ev Eşyaları", "Araba"};

        selectCategory.setItems(categories);
        selectCategory.setPlaceholder("Kategori Seçin");

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
        textFilter.setPlaceholder("Ürün girin");

        HorizontalLayout filterGroup = new HorizontalLayout();

        filterGroup.add(textFilter, btnFilter);

        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(textFilter.getValue());
            textFilter.setValue("");
        });
        //Fİlter Last
        //-----------------------------------------------------------------------------------------------------------------
        // Properties Save-Cancel Begin
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);

        Button btnSave = new Button("Kaydet");
        Button btnCancel = new Button("Çık");

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
            try {
                product.setImage(IOUtils.toByteArray(buffer.getInputStream(image.getTitle().get())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setImageFileName(image.getTitle().get());
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
        Button btnEkle = new Button("Ürün Ekle", VaadinIcon.INSERT.create());
        btnEkle.addClickListener(buttonClickEvent -> {
            dialog.open();

        });

        grid.addItemClickListener(productItemClickEvent -> {

                    TextArea txtUser1 = new TextArea();
                    txtUser1.setLabel("İsim");
                    txtUser1.setValue(productItemClickEvent.getItem().getUser().getFirstName().toString());
                    txtUser1.setReadOnly(true);

                    TextArea txtCity1 = new TextArea();
                    txtCity1.setLabel("Şehir");
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
                    txtCityDistrict1.setLabel("İlçe");
                    txtCityDistrict1.setValue(productItemClickEvent.getItem().getCityDistrict().toString());
                    txtCityDistrict1.setReadOnly(true);

                    TextArea txtPrice1 = new TextArea();
                    txtPrice1.setLabel("Fiyat");
                    txtPrice1.setValue(productItemClickEvent.getItem().getPrice().toString());
                    txtPrice1.setReadOnly(true);

                    TextArea txtDescription1 = new TextArea();
                    txtDescription1.setLabel("Açıklama");
                    txtDescription1.setValue(productItemClickEvent.getItem().getDescription().toString());
                    txtDescription1.setReadOnly(true);

                    TextArea txtNumberofView1 = new TextArea();
                    txtNumberofView1.setLabel("Görüntülenme Sayısı");
                    txtNumberofView1.setValue(productItemClickEvent.getItem().getNumberOfViews().toString());
                    txtNumberofView1.setReadOnly(true);
                    Image image1 = new Image();
                    image1.setSrc("");


                    if (productItemClickEvent.getItem().getImageFileName() != null) {
                        StreamResource resource = new StreamResource(productItemClickEvent.getItem().getImageFileName(), () -> new ByteArrayInputStream((productItemClickEvent.getItem().getImage())));
                        image1.setSrc(resource);
                        image1.setWidth("100px");
                        image1.setHeight("100px");
                    }


                    Button updateClickBtn = new Button("Güncelle");
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
                            image1.setSrc(resource);
                        }
                        dialog.open();//formlayout
                    });

                    clickDialog.open();
                    Button cancelclickBtn = new Button("Kapat");

                    Button messageClickBtn = new Button("Mesaj");

                    Button sendMessageBtn = new Button("Gönder");
                    Button cancelMessageBtn = new Button("Kapat");
                    TextArea textMessage = new TextArea();

                    messageClickBtn.addClickListener(buttonClickEvent -> {

                        messageDialog.open();
                        messageDialog.add(textMessage, sendMessageBtn, cancelMessageBtn);

                        textMessage.setWidth("200px");
                        textMessage.setWidth("200px");
                    });
                    sendMessageBtn.addClickListener(buttonClickEvent -> {
                        Message message1 = new Message();
                        message1.setMessageText(textMessage.getValue());
                        message1.setUser(productItemClickEvent.getItem().getUser());
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
                    //kendi girişe mesaj atamasın
                    if (productItemClickEvent.getItem().getUser().getId() != userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId()) {
                        clickDialog.add(messageClickBtn);
                    } else {
                        clickDialog.add(updateClickBtn);
                    }

                    cancelclickBtn.addClickListener(buttonClickEvent -> {
                        clickDialog.remove(txtUser1, txtCity1, txtCategory1, txtAdress1, txtCityDistrict1, txtPrice1, txtDescription1, txtNumberofView1, image1, cancelclickBtn);
                        //kendi girişinde dialogdan silinsin.
                        if (productItemClickEvent.getItem().getUser().getId() != userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId()) {
                            clickDialog.remove(messageClickBtn);
                        } else {
                            clickDialog.remove(updateClickBtn);
                        }

                        refreshData();
                        clickDialog.close();
                    });
                    //Görüntülenme Sayisi
                    int x = productItemClickEvent.getItem().getNumberOfViews();
                    x = x + 1;
                    productItemClickEvent.getItem().setNumberOfViews(x);
                    productService.save(productItemClickEvent.getItem());//productItemClickEvent.getItem()=>return select row product
                }
        );
        Button btnMyProduct = new Button("Ürünlerim");
        btnMyProduct.addClickListener(buttonClickEvent -> {
            refreshData(userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId());
        });
        Button btnAllProduct = new Button("Tüm Ürünler");
        btnAllProduct.addClickListener(buttonClickEvent -> {
            refreshData();
        });
        //CategoryFilter begin
        Button btnCategoryFilter = new Button("Kategori Ara", VaadinIcon.FILTER.create());
        ComboBox comboBoxCategoryFilter = new ComboBox<>();
        comboBoxCategoryFilter.setPlaceholder("Kategori Seçin");
        comboBoxCategoryFilter.setItems(categories);

        HorizontalLayout btnCategoryFilterGruop = new HorizontalLayout();

        btnCategoryFilterGruop.add(comboBoxCategoryFilter, btnCategoryFilter);

        btnCategoryFilter.addClickListener(buttonClickEvent -> {
            refreshCategoryData(comboBoxCategoryFilter.getValue().toString());
            comboBoxCategoryFilter.setValue("");
        });
        //category filter last

        //My Message
        Button myMessageButton = new Button("Mesajlarım ");
        Button cancelMyMessageButton = new Button("Kapat");
        Dialog myMessageDialog = new Dialog();
        TextArea textAreaMessage = new TextArea();
        textAreaMessage.setReadOnly(true);

        myMessageButton.addClickListener(buttonClickEvent -> {
            textAreaMessage.setValue(String.valueOf(refreshMessageData(userService.findUser(Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInUserId").toString())).get().getId())));
            myMessageDialog.add(textAreaMessage, cancelMyMessageButton);
            myMessageDialog.open();
        });
        cancelMyMessageButton.addClickListener(buttonClickEvent -> {
            myMessageDialog.close();
        });
        //my message last
        Button btnLogout = new Button("Çıkış Yap");
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
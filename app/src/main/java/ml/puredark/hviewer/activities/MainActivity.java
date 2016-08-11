package ml.puredark.hviewer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.puredark.hviewer.HViewerApplication;
import ml.puredark.hviewer.R;
import ml.puredark.hviewer.adapters.SiteAdapter;
import ml.puredark.hviewer.beans.Rule;
import ml.puredark.hviewer.beans.Selector;
import ml.puredark.hviewer.beans.Site;
import ml.puredark.hviewer.dataproviders.AbstractDataProvider;
import ml.puredark.hviewer.dataproviders.ListDataProvider;
import ml.puredark.hviewer.fragments.CollectionFragment;
import ml.puredark.hviewer.fragments.MyFragment;
import ml.puredark.hviewer.utils.FileUtils;
import ml.puredark.hviewer.utils.SharedPreferencesUtil;
import ml.puredark.hviewer.utils.SimpleFileUtil;

import static ml.puredark.hviewer.activities.SettingActivity.SettingFragment.KEY_PREF_PROXY_ENABLED;
import static ml.puredark.hviewer.activities.SettingActivity.SettingFragment.KEY_PREF_PROXY_PICTURE;
import static ml.puredark.hviewer.activities.SettingActivity.SettingFragment.KEY_PREF_PROXY_REQUEST;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_ADD_SITE;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_search)
    FloatingActionButton fabSearch;

    @BindView(R.id.nav_header_view)
    LinearLayout navHeaderView;
    @BindView(R.id.rv_site)
    RecyclerView rvSite;
    @BindView(R.id.btn_exit)
    LinearLayout btnExit;

    //记录当前加载的是哪个Fragment
    private MyFragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialize user settings
        //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // User interface
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        List<Site> sites = HViewerApplication.getSites();

//        sites.clear();
//
//        Rule indexRule = new Rule();
//        indexRule.item = new Selector("#ig .ig", null, null, null);
//        indexRule.idCode = new Selector("td.ii a", "attr", "href", "/g/(.*)");
//        indexRule.title = new Selector("table.it tr:eq(0) a", "html", null, null);
//        indexRule.uploader = new Selector("table.it tr:eq(1) td:eq(1)", "html", null, "(by .*)");
//        indexRule.cover = new Selector("td.ii img", "attr", "src", null);
//        indexRule.category = new Selector("table.it tr:eq(2) td:eq(1)", "html", null, null);
//        indexRule.datetime = new Selector("table.it tr:eq(1) td:eq(1)", "html", null, "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})");
//        indexRule.rating = new Selector("table.it tr:eq(4) td:eq(1)", "html", null, null);
//        indexRule.tags = new Selector("table.it tr:eq(3) td:eq(1)", "html", null, "([a-zA-Z0-9 -]+)");
//
//        Rule galleryRule = new Rule();
//        galleryRule.pictures = new Selector("#gh .gi a", null, null, "<a.*?href=\"(.*?)\".*?<img.*?src=\"(.*?)\"");
//
//        Selector pic = new Selector("img#sm", "attr", "src", null);
//
//        sites.add(new Site(1, "Lofi.E-hentai",
//                "http://lofi.e-hentai.org/?page={page:0}",
//                "http://lofi.e-hentai.org/g/{idCode:}/{page:0}",
//                "http://lofi.e-hentai.org/?f_search={keyword:}&page={page:0}",
//                indexRule, galleryRule, pic));
//
//        indexRule = new Rule();
//        indexRule.item = new Selector("table.itg tr.gtr0,tr.gtr1", null, null, null);
//        indexRule.idCode = new Selector("td.itd div div.it5 a", "attr", "href", "/g/(.*)");
//        indexRule.title = new Selector("td.itd div div.it5 a", "html", null, null);
//        indexRule.uploader = new Selector("td.itu div a", "html", null, null);
//        indexRule.cover = new Selector("td.itd div div.it2", "html", null, "(t/.*.jpg)");
//        indexRule.category = new Selector("td.itdc a img", "attr", "alt", null);
//        indexRule.datetime = new Selector("td.itd:eq(0)", "html", null, null);
//        indexRule.rating = new Selector("td.itd div div.it4 div", "attr||5-{1}/16", "style", "background-position:-(\\d+)px");
//
//        galleryRule = new Rule();
//        galleryRule.pictures = new Selector("#gh .gi a", null, null, "<a.*?href=\"(.*?)\".*?<img.*?src=\"(.*?)\"");
//
//        pic = new Selector("img#sm", "attr", "src", null);
//
//        sites.add(new Site(2, "G.E-hentai",
//                "http://g.e-hentai.org/?page={page:0}",
//                "http://g.e-hentai.org/g/{idCode:}/?p={page:0}",
//                "http://g.e-hentai.org/?f_search={keyword:}&page={page:0}",
//                indexRule, galleryRule, pic));
//
//        indexRule = new Rule();
//        indexRule.item = new Selector("div.gallary_wrap ul li.gallary_item", null, null, null);
//        indexRule.idCode = new Selector("div.pic_box a", "attr", "href", "aid-(\\d+)");
//        indexRule.title = new Selector("div.info div.title a", "html", null, null);
//        indexRule.cover = new Selector("div.pic_box a img", "attr", "data-original", null);
//        indexRule.datetime = new Selector("div.info div.info_col ", "html", null, "(\\d{4}-\\d{2}-\\d{2})");
//
//        galleryRule = new Rule();
//        galleryRule.pictures = new Selector("div.gallary_wrap ul li.gallary_item div.pic_box", "html", null, "<a.*?href=\"(.*?)\".*?<img.*?data-original=\"(.*?)\"");
//
//        pic = new Selector("img#picarea", "attr", "src", null);
//
//        sites.add(new Site(3, "绅士漫画",
//                "http://www.wnacg.org/albums-index-page-{page:1}.html",
//                "http://www.wnacg.org/photos-index-page-{page:1}-aid-{idCode:}.html",
//                "http://www.wnacg.org/albums-index-page-{page:1}-sname-{keyword:}.html",
//                indexRule, galleryRule, pic));


        AbstractDataProvider<Site> dataProvider = new ListDataProvider<>(sites);
        final SiteAdapter adapter = new SiteAdapter(dataProvider);
        rvSite.setAdapter(adapter);

        adapter.setOnItemClickListener(new SiteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (position == adapter.getItemCount() - 1) {
                    Intent intent = new Intent(MainActivity.this, AddSiteActivity.class);
                    startActivityForResult(intent, RESULT_ADD_SITE);
                } else {
                    Site site = (Site) adapter.getDataProvider().getItem(position);
                    adapter.selectedSid = site.sid;
                    adapter.notifyDataSetChanged();
                    HViewerApplication.temp = site;
                    replaceFragment(CollectionFragment.newInstance(), site.title);
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        if (sites.size() > 0) {
            Site site = sites.get(0);
            adapter.selectedSid = site.sid;
            adapter.notifyDataSetChanged();
            HViewerApplication.temp = site;
            replaceFragment(CollectionFragment.newInstance(), site.title);
        }

    }

    @OnClick(R.id.fab_search)
    void search() {
        final EditText inputSearch = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server").setIcon(R.drawable.ic_search_white).setView(inputSearch)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String keyword = inputSearch.getText().toString();
                if (!"".equals(keyword) && currFragment != null)
                    currFragment.onSearch(keyword);
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_ADD_SITE) {
                int sid = data.getIntExtra("sid", 0);
                List<Site> sites = HViewerApplication.getSites();
                SiteAdapter adapter = ((SiteAdapter) rvSite.getAdapter());
                adapter.setDataProvider(new ListDataProvider(sites));
                adapter.selectedSid = sid;
                adapter.notifyDataSetChanged();
                Site site = sites.get(sites.size()-1);
                HViewerApplication.temp = site;
                replaceFragment(CollectionFragment.newInstance(), site.title);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(MyFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
        currFragment = fragment;
    }

    @OnClick(R.id.btn_setting)
    void openSetting() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_exit)
    void exit() {
        finish();
    }
}

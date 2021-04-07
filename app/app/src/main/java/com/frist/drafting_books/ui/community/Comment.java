package com.frist.drafting_books.ui.community;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frist.drafting_books.R;

import java.util.ArrayList;
import java.util.List;

public class Comment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initComment();
    }
    void initComment(){
        RecyclerView recyclerView=findViewById(R.id.comment);
        List<String> list=new ArrayList<>();
        list.add("版权归作者所有，任何形式转载请联系作者。\n" +
                "作者：风君（来自豆瓣）\n" +
                "来源：https://book.douban.com/review/5799892/\n" +
                "\n" +
                "\n" +
                "《人间失格》书成当年，太宰治旋即投水自尽。这部遗作，也因此在太宰的作品之中占有举足轻重的地位，被认为是作家一生遭遇与心路历程的映射。史铁生曾说过：“写作者，未必能塑造出真实的他人，写作者只可能塑造真实的自己。”这话用在太宰治身上可谓恰如其分，纵观他的各部作品中那诸多角色，不啻为他自己的无数分身。而在《人间失格》里，这种自我写照实在过于明显，以至于对太宰治略有了解的读者根本无需分析，就可以看出书中主角“大庭叶藏”其实就是“津岛修治”，亦即作者本人的化身。书中以叶藏独白道出的经历，与作者本人的人生重合度之高，令这部作品也被冠以“自传体小说”之名。鉴于其“遗作”的特殊地位，本书可看作是太宰治本人对自己人生的某种“总结”，窥探其内心世界的最后机会。在本书中，作者依旧一如既往地描写了一个被社会排斥的“边缘人”角色的挣扎与沉沦。而若要问本书与太宰其它作品相比最显著的特点，或者“相同之中的些许不同”是什么的话，恐怕只能说，本作是刻画太宰治“丑角精神”最深入、最全面也最彻底的一部作品。\n");
        list.add("版权归作者所有，任何形式转载请联系作者。\n" +
                "作者：渔夫和鱼（来自豆瓣）\n" +
                "来源：https://book.douban.com/review/1074604/\n" +
                "\n" +
                "\n" +
                "然死生之事，于作家亦莫大焉。想象这样一个作家，一生以毁减生命为志业，从二十岁起五度自杀，终于在四十岁前了结生命；想象这样一个作家，毕生的写作都是以自身为蓝本，自传体式的回忆贯串文本；想象这样一个作家，生前毁誉参半，死去近六十年还被不断追忆，连忌日都成为节日…… 他就是太宰治，日本无赖派大师，在日本与川端康成，三岛由纪夫并列战后文学的巅峰人物。\n" +
                "　　 一\n" +
                "　　太宰治，本名津岛修治，1909年出生于日本青森县北津轻郡贵族家庭，父亲是当地首要人物，曾任众议院议员、贵族院议员，母亲体弱多病，不能亲自抚养孩子，所以在十个孩子中排行第九的太宰治自小他由姑母及保母照顾，父亲的严厉与母亲的缺位让太宰治从小心思纤细而敏感。太宰治在初中时后开始创办同人刊物，从此决心以文学为业。1930年至东京大学法文系就读，师从井伏鳟二。大学时期太宰治积极参加左翼运动，同时开始过着放浪不羁的生活，曾与艺妓同居，毕业后走向消极，其间四次殉情未遂，三十九岁时与最后一位爱人相约投水自尽。1935年太宰治以《丑角之舞》初登文坛，短篇《逆行》亦入围芥川奖，后来继续出版不少的作品集，其中尤其以晚期的《斜阳》与《人间失格》为人称道，被誉为战后日本文学的金字塔作品。");
        CommentAdapter adapter=new CommentAdapter(this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}
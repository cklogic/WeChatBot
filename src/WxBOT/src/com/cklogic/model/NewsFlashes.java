package com.cklogic.model;

public class NewsFlashes {

    public int code;
    public Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        public QuickNew[] items;

		public QuickNew[] getItems() {
			return items;
		}

		public void setItems(QuickNew[] items) {
			this.items = items;
		}

		

		
	

        
    }

    public class QuickNew{
        public int id;
        public String title;
        public String description;
        public String news_url;
        public String created_at;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getNews_url() {
			return news_url;
		}

		public void setNews_url(String news_url) {
			this.news_url = news_url;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
        
    }
}
